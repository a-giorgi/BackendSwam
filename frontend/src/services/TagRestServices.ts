import { Vue } from 'vue-class-component';
import axios, { Method } from 'axios';
import TagDTO from '@/DTO/TagDTO';
import {backendRestUrl, tagResourceUrl, backendTimeout} from '@/services/endpoints';
import {JsonStringifyReplacer} from '@/utils/JsonStringifyReplacer';


export default class TagRestServices extends Vue{
    fetchAllTags(): Promise<any>{
        return axios.request({
            url: tagResourceUrl,
            timeout: backendTimeout,
            method: 'get',
            baseURL: backendRestUrl,
            headers: {
                'Authorization': 'Bearer '+ this.$store.getters.getToken
            }
        });
    }

    fetchActiveTags(): Promise<any>{
      return axios
          .get(backendRestUrl+'/'+tagResourceUrl+'/active');
    }
    
    saveTag(tagDto: TagDTO): Promise<any>{
      let method: Method = "post";
      let resourceSaveUrl = tagResourceUrl;
      if(tagDto.id !== null){
          method = "patch";
          resourceSaveUrl = tagResourceUrl + '/' + tagDto.id;
      }
      return axios.request({
        url: resourceSaveUrl,
        timeout: backendTimeout,
        method: method,
        baseURL: backendRestUrl,
        headers: {
            'Authorization': 'Bearer '+ this.$store.getters.getToken,
            'Content-Type': 'application/json',
        },
        data: JSON.stringify(tagDto,JsonStringifyReplacer),
      });

    }

    deleteTag(tagId: number): Promise<any>{
      return axios.request({
        url: tagResourceUrl+'/'+tagId,
        timeout: backendTimeout,
        method: 'delete',
        baseURL: backendRestUrl,
        headers: {
            'Authorization': 'Bearer '+ this.$store.getters.getToken
        }
      })
    }

    decodeTagResponseError(error: string, statusCode: number): string{
      switch(statusCode){
        case 400:
          if(error == "400-1"){
            return "L'URL fornito non è un Feed RSS valido!";
          }else if(error == "400-2"){
            return "Impossibile salvare il feed!";
          }else{
            return "Richiesta non valida!"
          }
        case 409:
          return "Questo Tag esiste già!";
      }
      return "Errore sconosciuto";
    }
}
