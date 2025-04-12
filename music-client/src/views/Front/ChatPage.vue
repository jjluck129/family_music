<template>
  <div class="chat-page">
    <el-row>
      <el-col :span="4">
        <el-card class="chat-user">
          <div class="chat-user-header">
            在线用户<span>( 点击气泡开始聊天 )</span>
          </div>
          <div class="chat-user-content" v-for="user in users" :key="user.username">
            <span :class="{ 'offline-user': user.status }">{{ user.username }}</span>
            <i class="fa-regular fa-comment-dots" @click="selectChatUser(user.username)"></i>
            <span class="chat-user-content-chatting" v-if="user.username === chatUser">聊天中...</span>
            <span v-if="unreadCounts[user.username] > 0" class="unread-badge">{{ unreadCounts[user.username] }}</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="20">
        <div class="chat-content">
          <div class="chat-content-header">聊天室 - {{ chatUser }}</div>
          <div class="chat-content-middle" ref="chatContainer">
            <div v-if="messages.length === 0" class="empty-message">暂无聊天记录</div>
            <div v-for="(message, index) in messages" :key="message.id ? message.id : index" class="message-row">
              <div
                class="message"
                :class="{ 'message-right': message.fromUser === user.username, 'message-left': message.fromUser !== user.username }"
              >
                <el-avatar
                  class="message-avatar"
                  size="medium"
                  :src="message.fromUser === user.username ? user.avatar : defaultAvatar"
                ></el-avatar>
                <div class="message-text">
                  {{ message.message }}
                  <!-- 如果是自己发送的消息，则显示读/未读状态 -->
                  <span v-if="message.fromUser === user.username">
                    <i v-if="message.status === 0" class="fa fa-clock-o" title="未读"></i>
                    <i v-else class="fa fa-check" title="已读"></i>
                  </span>
                </div>
              </div>
            </div>
          </div>
          <div class="chat-content-bottom" >
            <textarea v-model="text" class="chat-content-bottom-textarea" placeholder="请输入消息内容..."></textarea>
            <div class="chat-content-bottom-send">
              <el-button type="primary" size="mini" @click="send">发送</el-button>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'ChatPage',
  data() {
    return {
      user: JSON.parse(localStorage.getItem("family-user") || "{}"),
      defaultAvatar: "https://example.com/default-avatar.png",
      users: [],
      chatUser: '',
      text: "",
      messages: [],
      unreadCounts: {},
      socket: null,
    };
  },
  created() {
    this.init();
    this.getAllUsers();
  },
  watch:{
    messages(){
      this.scrollToBottom();
    }
  },
  methods: {
    // 初始化 WebSocket 连接
    init() {
      const username = this.user.username;
      if (typeof WebSocket === "undefined") {
        console.log("您的浏览器不支持WebSocket");
        return;
      }
      const socketUrl = "ws://localhost:9090/imserver/" + username;
      if (this.socket) {
        this.socket.close();
      }
      this.socket = new WebSocket(socketUrl);
      this.socket.onopen = () => {
        console.log("WebSocket服务已打开");
      };
      this.socket.onmessage = (msg) => {
        console.log("收到数据===" + msg.data);
        this.handleMessage(msg.data);
      };
      this.socket.onclose = () => {
        console.log("WebSocket服务已关闭");
      };
      this.socket.onerror = () => {
        console.log("WebSocket服务发生错误");
      };
    },
    // 处理服务器返回的消息
    handleMessage(data) {
      try {
        const parsedData = JSON.parse(data);
        // 如果是用户列表更新
        if (parsedData.users) {
          this.users = parsedData.users.filter(u => u.username !== this.user.username);
          return;
        }
        // 处理读回执消息
        if (parsedData.type && parsedData.type === 'readReceipt') {
          // 当当前用户为原消息发送者，即 readReceipt 中的 to 字段
          if (this.user.username === parsedData.to) {
            this.messages.forEach(message => {
              if (message.fromUser === this.user.username &&
                  message.toUser === parsedData.from &&
                  message.status === 0) {
                message.status = 1;
              }
            });
          }
          return;
        }
        // 处理普通聊天消息
        if (parsedData.fromUser && parsedData.message) {
          // 如果当前选中的聊天对象和消息的发送方或接收方匹配，则显示在聊天框中
          if (this.chatUser && (parsedData.fromUser === this.chatUser || parsedData.toUser === this.chatUser)) {
            this.messages.push(parsedData);
            this.scrollToBottom();
          } else {
            // 否则累计未读消息计数
            this.unreadCounts[parsedData.fromUser] = (this.unreadCounts[parsedData.fromUser] || 0) + 1;
          }
        }
      } catch (e) {
        console.error("处理消息异常", e);
      }
    },
    // 发送消息，并清空输入框
    send() {
      if (!this.chatUser) {
        this.$message({ type: 'warning', message: "请选择聊天对象" });
        return;
      }
      if (!this.text.trim()) {
        this.$message({ type: 'warning', message: "请输入内容" });
        return;
      }
      const messageObj = {
        fromUser: this.user.username,
        toUser: this.chatUser,
        message: this.text.trim(),
        status: 0
      };
      // 发送消息给服务器
      this.socket.send(JSON.stringify(messageObj));
      // 立即显示在聊天框中
      this.messages.push(messageObj);
      // 清空发送框
      this.text = "";
      this.scrollToBottom();
    },
    // 选择聊天对象：加载历史记录、清空未读计数并发送读回执
    selectChatUser(username) {
      this.chatUser = username;
      this.messages = [];
      this.unreadCounts[username] = 0;
      this.loadChatHistory(username);
      const readReceipt = {
        type: 'readReceipt',
        from: this.user.username,
        to: username
      };
      this.socket.send(JSON.stringify(readReceipt));
    },
    // 通过 REST 接口加载历史聊天记录
    loadChatHistory(withUser) {
      console.log(this.user.username+"&&"+withUser)
      this.$request.get(`/chat/history?user=${this.user.username}&with=${withUser}`)
        .then(response => {
          console.log("收到的数据为："+response.data)
          this.messages = response.data;
          console.log(this.messages)
        })
        .catch(error => {
          console.error("加载历史消息失败", error);
        });
    },
    // 获取所有用户（过滤掉当前用户）
    getAllUsers() {
      this.$request.get(`/users`)
        .then(response => {
          this.users = response.data.filter(u => u.username !== this.user.username);
        })
        .catch(error => {
          console.error("加载用户列表失败", error);
        });
    },
    scrollToBottom() {
    this.$nextTick(() => {
      const chatContainer = this.$refs.chatContainer;
      if (chatContainer) {
        chatContainer.scrollTop = chatContainer.scrollHeight;
      }
    });
  },
  }
}
</script>

<style scoped>
.chat-page {
  padding: 10px;
  margin-bottom: 50px;
}
.chat-user {
  width: 300px;
  height: 300px;
  color: #333;
}
.chat-user-header {
  padding-bottom: 10px;
  border-bottom: 1px solid #ccc;
}
.chat-user-header span {
  font-size: 12px;
}
.chat-user-content {
  padding: 10px 0;
}
.chat-user-content i {
  margin-left: 10px;
  font-size: 16px;
  cursor: pointer;
}
.chat-user-content-chatting {
  font-size: 12px;
  color: limegreen;
  margin-left: 5px;
}
.offline-user {
  color: blue;
  font-size: large;
}
.unread-badge {
  background-color: red;
  color: white;
  border-radius: 50%;
  padding: 2px 6px;
  font-size: 10px;
}
.chat-content {
  width: 800px;
  margin: 0 auto;
  background-color: white;
  border-radius: 5px;
  box-shadow: 0 0 10px #ccc;
}
.chat-content-header {
  text-align: center;
  line-height: 50px;
}
.chat-content-middle {
  height: 350px;
  overflow-y: auto;
  border-top: 1px solid #ccc;
  padding: 10px;
}
.empty-message {
  text-align: center;
  color: #999;
  margin-top: 20px;
}
.message-row {
  display: flex;
  margin-bottom: 10px;
  margin-right: 30px;
}
.message-left {
  justify-content: flex-start;
}
.message-right {
  position: relative;
  left: 85%;
}
.message {
  display: flex;
  align-items: center;
}
.message-avatar {
  margin: 0 10px;
}
.message-text {
  background-color: #f0f0f0;
  padding: 8px 12px;
  border-radius: 10px;
  max-width: 70%;
  word-wrap: break-word;
}
.message-right .message-text {
  background-color: #cce5ff;
}
.chat-content-bottom {
  padding: 10px;
  border-top: 1px solid #ccc;
}
.chat-content-bottom-textarea {
  height: 80px;
  width: 100%;
  padding: 10px 0 0 0;
  border: 1px solid #ccc;
  border-radius: 4px;
  resize: none;
}
.chat-content-bottom-send {
  text-align: right;
  margin-top: 10px;
}
</style>
