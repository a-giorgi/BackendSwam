<template>
<div>
<br>
  <div class="mdc-card compact" :style="style" >
    <div class="card-body">
      <h3 class="card-title m-3 font bold">Registrati</h3><hr class="soft"><br>
      

        <span > Inserisci i tuoi dati d'accesso</span><br><br>

        <label id="usertext" class="mdc-text-field mdc-text-field--filled">
        <span class="mdc-text-field__ripple"></span>
        <span class="mdc-floating-label" id="userlabel">Username</span>
        <input class="mdc-text-field__input" v-model="username" type="text"  maxlength="25" aria-labelledby="userlabel">
        <span class="mdc-line-ripple"></span>
        </label>
      
        <br><br>

        <label id="mailtext" class="mdc-text-field mdc-text-field--filled">
        <span class="mdc-text-field__ripple"></span>
        <span class="mdc-floating-label" id="maillabel">E-Mail</span>
        <input class="mdc-text-field__input" v-model="email" type="text"  maxlength="50" aria-labelledby="maillabel">
        <span class="mdc-line-ripple"></span>
        </label>
      
      
        <br><br>
        <label id="passwordtext" class="mdc-text-field mdc-text-field--filled">
        <span class="mdc-text-field__ripple"></span>
        <span class="mdc-floating-label" id="passwordlabel">Password</span>
        <input class="mdc-text-field__input" v-model="password" type="password"  maxlength="25" aria-labelledby="passwordlabel">
        <span class="mdc-line-ripple"></span>
        </label>

        <br><br>
        <label id="confirmPasswordtext" class="mdc-text-field mdc-text-field--filled">
        <span class="mdc-text-field__ripple"></span>
        <span class="mdc-floating-label" id="confirmPasswordlabel">Conferma Password</span>
        <input class="mdc-text-field__input" v-model="confirmPassword" type="password"  maxlength="25" aria-labelledby="passwordlabel">
        <span class="mdc-line-ripple"></span>
        </label>
        <br>

        <div style="display:inline-block" :class="{red: isError, green: isSuccess}">{{output}}</div><br>
        
        <a class="small pointer" @click="$emit('login')"><u>Hai già un'account? Clicca qui per accedere!</u></a>
        <br>
        <div class="mdc-card__actions right">
            <button @click="submitCard" :disabled="isButtonDisabled" class="mdc-button mdc-card__action mdc-card__action--button">
              <div class="mdc-button__ripple"></div>
              <span class="mdc-button__label">Registrati</span>
            </button>
        </div>
    </div>
  </div>
</div>



</template>

<script lang="ts">
import { Options,  mixins } from 'vue-class-component';
import {MDCTextField} from '@material/textfield';
import UserDTO from '@/DTO/UserDTO';
import "@material/card/dist/mdc.card.css";
import "material-components-web/dist/material-components-web.js";
import "material-components-web/dist/material-components-web.css";
import UserRestServices from '@/services/UserRestServices';

/*
import "@material/floating-label/dist/mdc.floating-label.css"
import "@material/floating-label/dist/mdc.floatingLabel.js"

import "@material/line-ripple/dist/mdc.line-ripple.css"
import "@material/line-ripple/dist/mdc.lineRipple.js"

import "@material/notched-outline/dist/mdc.notchedOutline.js"
import "@material/notched-outline/dist/mdc.notched-outline.css"

import "@material/textfield/dist/mdc.textfield.js"
import "@material/textfield/dist/mdc.textfield.css"*/
@Options({
 props: {
    width: String,
    fromLeft: String
  },
  emits: {
    login: false,
  }
})
export default class SignUpController extends mixins(UserRestServices) {
  [x: string]: any;
    width!: string;
    fromLeft!: string;
    username = "";
    email = "";
    password = "";
    confirmPassword = "";
    output = "";
    isError = false;
    isSuccess = false;
    isButtonDisabled = false;
 
  mounted() {
    //context required for material design to work
    const userField = MDCTextField.attachTo(this.$el.querySelector('#usertext')!);
    const mailField = MDCTextField.attachTo(this.$el.querySelector('#mailtext')!);
    const pwdField = MDCTextField.attachTo(this.$el.querySelector('#passwordtext')!);
    const pwdConfirmField = MDCTextField.attachTo(this.$el.querySelector('#confirmPasswordtext')!);
    console.log(this.$store.getters.userData);
    
  }
  submitCard(){
    const isPasswordVerified: string | boolean = this.passwordVerify(this.password,this.confirmPassword);
    if(isPasswordVerified!=true){
      this.isError = true;
      this.isSuccess = false;
      this.output = isPasswordVerified.toString();
      return;
    }
    const userDto: UserDTO = {
      "id": null,
      "username": this.username,
      "email": this.email,
      "oldPassword": "",
      "password": this.password,
      "confirmPassword": this.confirmPassword,
      "countryCode": ""
    }
    this.isButtonDisabled = true;
    this.saveUser(userDto).then(response => {
          console.log("operazione effettuata con successo");
          const updated = response.data.resource as UserDTO;
          userDto.id = updated.id!;
          this.isError = false;
          this.isSuccess = true;
          this.output = "Utente Registrato! Attendi..."
          setTimeout(() => { this.$emit('login') }, 1000);
    }).catch(error => { 
        this.isButtonDisabled = false;
        if(error.code === 'ECONNABORTED'){
          this.$store.commit("sendMessage","Il backend sembra essere offline...<br> Riprova più tardi.");
          return;
        }
        this.output = this.decodeUserResponseError(error.response.data.code as string, error.response.status as number);
        this.isError = true;
        this.isSuccess = false;
    });
  }
  toLogin(){
      this.$router.push("login");
  }

  get style(){
        return "padding:5px; margin-bottom:10px;  left: 50%; width: " + this.width + " ;  margin-left:-" + this.fromLeft + ";";
      }
  
}
</script>

<style>


.compact{
  width: 18rem;
  line-height: 1.5rem;
  box-sizing: border-box;
}
.small{
  font-size: small;
}

.soft{
  border:0.5px solid lightgray;
  
}

.red{
    margin-bottom: 8px;
    margin-top:16px;
    padding-left:5px;
    padding-right:5px;
    background-color: rgba(255, 0, 0, 0.89);
    color: white;
    font-weight: bold;
    border: rgb(105, 0, 0);
    border-radius: 5%;
}

.green{
    margin-bottom: 8px;
    margin-top:16px;
    padding-left:5px;
    padding-right:5px;
    margin-top:8px;
    background-color: rgba(83, 255, 98, 0.486);
    color: rgb(0, 94, 16);
    border: green;
    font-weight: bold;
    border-radius: 5%;
}

.mt{
  margin-top:10px;
}


.pointer{
  cursor:pointer;
}

.card {
  box-shadow: 0 4px 8px 0 rgba(0,0,0,0.2);
  transition: 0.3s;

}
.right{
  float: right;
}

/* On mouse-over, add a deeper shadow */
.card:hover {
  box-shadow: 0 8px 16px 0 rgba(0,0,0,0.2);
}

/* Add some padding inside the card container */
.container {
  padding: 2px 16px;
}

.input-styled{
  display: block;
    width: 100%;
    padding: .375rem .75rem;
    font-size: 1rem;
    line-height: 1.5;
    color: #495057;
    background-color: #fff;
    background-clip: padding-box;
    border: 1px solid #ced4da;
    border-radius: .25rem;
    transition: border-color .15s ease-in-out,box-shadow .15s ease-in-out;
}
</style>



