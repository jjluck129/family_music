<template>
  <div class="chat-page">
    <el-row>
        <el-col :span="4">
          <el-card class="chat-user">
              <div class="chat-user-header">在线用户<span>( 点击气泡开始聊天 )</span>
              </div>
              <div class="chat-user-content" v-for="user in users" :key="user.username">
                <span>{{ user.username }}</span>
                <i class="fa-regular fa-comment-dots"  @click="chatUser = user.username"></i>
                <span class="chat-user-content-chatting" v-if="user.username === chatUser">chatting...</span>
              </div>
          </el-card>
        </el-col>
        <el-col :span="20">
          <div class="chat-content">
            <div class="chat-content-header">聊天室{{ chatUser }}</div>
            <div class="chat-content-middle">
              <div v-for="message in messages" :key="message.id" class="message-row">
                <div
                  class="message"
                  :class="{ 'message-right': message.user === user.username, 'message-left': message.user !== user.username }"
                >
                  <el-avatar
                    class="message-avatar"
                    size="medium"
                    :src="message.user === user.username ? user.avatar : defaultAvatar"
                  ></el-avatar>
                  <div class="message-text">{{ message.text }}</div>
                </div>
              </div>
            </div>
            <div class="chat-content-bottom">
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
let socket;
export default{
  name: 'ChatPage',
  data(){
    return {
      // i: JSON.parse(localStorage.getItem("family-user") || "{}").avatar,
      // user: [],
      user:JSON.parse(localStorage.getItem("family-user") || "{}"),
      circleUrl: "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
      // isCollapse: false,
      users: [],
      chatUser: '',
      text: "",
      messages: [],
      // content: '',
    }
  },
  created(){
    this.init()
  },
  methods:{
    send(){
      if(!this.chatUser) {
        this.$message({type: 'warning', message: "请选择聊天对象"})
        return;
      }
      if(!this.text) {
        this.$message({type: 'warning', message: "请输入内容"})
      } else {
        if(typeof (WebSocket) == "undefined") {
          console.log("您的浏览器不支持WebSocket");
        } else{
          console.log("您的浏览器支持WebSocket");
          let message = {from: this.user.username, to: this.chatUser,text:this.text}
          socket.send(JSON.stringify(message));
          // 添加本地消息到列表
          this.messages.push({user: this.user.username,text:this.text})
          this.text  = '';
        }
      }
    },
    init(){
      let username = this.user.username;
      if(typeof (WebSocket) == "undefined") {
        console.log("您的浏览器不支持WebSocket");
      } else {
        console.log("您的浏览器支持WebSocket");
        let socketUrl = "ws://localhost:9090/imserver/" + username;
        if(socket != null) {
          socket.close();
          socket = null;
        }
        //开启一个websocket服务
        socket = new WebSocket(socketUrl);
        //打开事件
        socket.onopen = function () {
          console.log("WebSocket服务已打开");
        }
        // 浏览器接受消息，获取服务端传来的消息
        socket.onmessage = (msg) => {
          console.log("收到数据===" + msg.data)
          this.handleMessage(msg.data);
        }
        //关闭事件
        socket.onclose = function (){
          console.log("WebSocket服务已关闭");
        };
        //发生错误
        socket.onerror = function (){
          console.log("WebSocket服务发生错误");

        }
      }
    },
    handleMessage(data) {
      let parsedData = JSON.parse(data);
      if (parsedData.users) {
        this.users = parsedData.users.filter((user) => user.username !== this.user.username);
      } else if (parsedData.from === this.chatUser) {
        this.messages.push({ id: Date.now(), user: parsedData.from, text: parsedData.text });
      }
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
.message-row {
  display: flex;
  margin-bottom: 10px;
}
.message-left {
  justify-content: flex-start;
}
.message-right {
  /* justify-content: flex-end; */
  position: relative;
  left: 85%
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
  background-color: #cce5ff; /* 自定义右侧消息背景色 */
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
