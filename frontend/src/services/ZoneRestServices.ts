import { Vue } from 'vue-class-component';
import axios from 'axios';
import {backendRestUrl,zoneResourceUrl,backendTimeout} from '@/services/endpoints';


export default class ZoneRestServices extends Vue{
    fetchAllZones(): Promise<any>{
        return axios.request({
            url: zoneResourceUrl,
            timeout: backendTimeout,
            method: 'get',
            baseURL: backendRestUrl,
            headers: {
                'Authorization': 'Bearer '+ this.$store.getters.getToken
            }
        });
    }
}
