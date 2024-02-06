import { InjectionKey } from 'vue';
import { createStore, useStore as baseUseStore, Store } from 'vuex';
import createPersistedState from 'vuex-persistedstate';
import ZoneDTO  from '@/DTO/ZoneDTO';
import TokenDTO from '@/DTO/TokenDTO';

// define your typings for the store state
export interface State {
  token: string | null;
  isTokenExpired: boolean; 
  zones: ZoneDTO[];
  online: boolean;
  message: string;
}



// define injection key
export const key: InjectionKey<Store<State>> = Symbol()

export const store = createStore<State>({
  state: {
        online: true,
        token: null,
        isTokenExpired: false,
        zones: [],
        message: "",
  },
  getters: {
    // ...
    isLoggedIn (state, getters) {
        if(state.token!=null){
            return true;
        }
        return false;
    },
    isOnline(state, getters){
      return state.online;
    },
    isZonesLoaded(state, getters){
      if(state.zones.length>0){
        return true;
      }
      return false;
    },
    getToken(state, getters){
      if(state.token==null){
        return null;
      }
      const splitted = state.token.split("."); 
      const tokenInfo: TokenDTO = JSON.parse(atob(splitted[1]));
    
      const time = new Date().getTime();
      const date = new Date(time);
    

      let utcExpiry = tokenInfo.expiryDate.replace(" ", "T");
      utcExpiry = utcExpiry + ".000+00:00";
      //console.log("ExpiryDate: " + utcExpiry);

      const expiry = Date.parse(utcExpiry);
      const now = Date.parse(date.toUTCString());

      //console.log(expiry);
      //console.log(now);

      if(now<expiry){
        return state.token;
      }
      state.isTokenExpired = true;
      return null;
    },
    userData(state,getters){
      if(state.token==null){
        return null;
      }
      const splitted = state.token.split("."); 
      return atob(splitted[1]);

    },
    isAdmin(state,getters): boolean{
      if(state.token==null){
        return false;
      }
      const splitted = state.token.split("."); 
      const tokenInfo: TokenDTO = JSON.parse(atob(splitted[1]));
      if(tokenInfo.isAdmin==true){
        return true;
      }
      return false;
    },
    isTokenExpired(state,getters): boolean{
      if(state.token==null){
        return false;
      }
      return state.isTokenExpired;
    },
    getZones(state, getters): ZoneDTO[]{
      return state.zones;
    },
    eventMessage(state, getters): string{
      return state.message;
    }
  },
  mutations: {
    storeToken (state, token) {
        state.token = token;
    },
    logout(state){
        state.token = null;
        state.zones = [];
        state.isTokenExpired = false;
    },
    storeZones(state, loadedZones: ZoneDTO[]){
      state.zones = loadedZones;
    },
    changeOnline(state, status: boolean){
      state.online = status;
    },
    sendMessage(state, message: string){
      state.message = message;
    },
    resetMessage(state){
      state.message = "";
    }
  },
  plugins: [
    createPersistedState({
    })
  ]
});

export function useStore () {
    return baseUseStore(key);
  }