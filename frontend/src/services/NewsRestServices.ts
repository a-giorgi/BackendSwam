import { Vue } from 'vue-class-component';
import axios from 'axios';
import {backendRestUrl,newsResourceUrl,backendTimeout} from '@/services/endpoints';


export default class NewsRestServices extends Vue{
    //this has more timeout
    fetchAllNews(){
        return axios.request({
            url: newsResourceUrl,
            timeout: backendTimeout*3,
            method: 'get',
            baseURL: backendRestUrl,
            headers: {
                'Authorization': 'Bearer '+ this.$store.getters.getToken
            }
        });
    }
    deleteNewsCache(){
        return axios.request({
            url: newsResourceUrl,
            timeout: backendTimeout,
            method: 'delete',
            baseURL: backendRestUrl,
            headers: {
                'Authorization': 'Bearer '+ this.$store.getters.getToken
            }
        });
    }
}