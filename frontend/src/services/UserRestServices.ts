import { Vue } from 'vue-class-component';
import axios, { Method } from 'axios';
import UserDTO from '@/DTO/UserDTO';
import {backendRestUrl,userResourceUrl, backendTimeout} from '@/services/endpoints'
import {JsonStringifyReplacer} from '@/utils/JsonStringifyReplacer';


export default class UserRestServices extends Vue{
    saveUser(userDto: UserDTO): Promise<any>{
      let method: Method = "post";
      let specificResourceUrl = userResourceUrl;
      if(userDto.id !== null){
          method = "patch";
          specificResourceUrl = userResourceUrl+"/"+ userDto.id;  
      }
        return axios.request({
          url: specificResourceUrl,
          timeout: backendTimeout,
          method: method,
          baseURL: backendRestUrl,
          headers: {
              'Authorization': 'Bearer '+ this.$store.getters.getToken,
              'Content-Type': 'application/json',
          },
          data: JSON.stringify(userDto,JsonStringifyReplacer),
        });  
    }
    login(username, password): Promise<any>{
        return axios.post(backendRestUrl+userResourceUrl+"/login", {
                username: username,
                password: password,
            });           
    }
    resetUserPassword(userDto: UserDTO): Promise<any>{
      const method: Method = "patch";
      const specificResourceUrl = userResourceUrl+'/'+userDto.id+"/reset";
      return axios.request({
        url: specificResourceUrl,
        timeout: backendTimeout,
        method: method,
        baseURL: backendRestUrl,
        headers: {
            'Authorization': 'Bearer '+ this.$store.getters.getToken,
            'Content-Type': 'application/json',
        },
        data: JSON.stringify(userDto,JsonStringifyReplacer)
      });  
    }
    getUserInfo(userId: number): Promise<any>{
      const method: Method = "get";
      return axios.request({
        url: userResourceUrl+'/'+userId,
        timeout: backendTimeout,
        method: method,
        baseURL: backendRestUrl,
        headers: {
            'Authorization': 'Bearer '+ this.$store.getters.getToken,
            'Content-Type': 'application/json',
        },
      });  
    }
    saveCountryCode(userDto: UserDTO): Promise<any>{
      const method: Method = "patch";
      return axios.request({
        url: userResourceUrl+'/'+userDto.id,
        timeout: backendTimeout,
        method: method,
        baseURL: backendRestUrl,
        headers: {
            'Authorization': 'Bearer '+ this.$store.getters.getToken,
            'Content-Type': 'application/json',
        },
        data: JSON.stringify(userDto,JsonStringifyReplacer)
      });  
    }
    passwordVerify(pwd: string, confirm: string): string | boolean{
        if(pwd!=confirm){
           return "Le password non corrispondono";
        }
        if(pwd.length<8){
            return "La password deve contenere almeno 8 caratteri"
        }
        if(pwd.includes(" ")){
            return "La password non può contenere spazi"
        }
        return true;
    }
    decodeUserResponseError(error: string, statusCode: number): string{
      switch(statusCode){
        case 400:
          if(error == "400-1"){
            return "I dati inseriti non sono validi";
          }else if(error == "400-2"){
            return "La password non soddisfa i requisiti di sicurezza richiesti";
          }else if(error == "400-3"){
              return "La vecchia password non è presente";
          }else if(error == "400-4"){
            return "L'username è vuoto!";
          }else if(error == "400-5"){
            return "La password è vuota!";
          }else{
              return "Richiesta non valida!"
          }
          break;
        case 409:
          return "Esiste già un utente avente l'email o l'username inserito!";
          break;
        case 403:
            return "Dati non corretti!";
          break;
        case 404:
          if(error == "404-2"){
            return "Utente non trovato!";
          }else {
            return "Accesso non consentito!";
          }
          break;
      }
      return "Errore sconosciuto";
    }
}