import { Vue } from 'vue-class-component';



export default class InternetServices extends Vue{
    verifyOnline(): boolean{
        const online = navigator.onLine ? true : false; 
        this.$store.commit("changeOnline", online);
        return online;
    }

}