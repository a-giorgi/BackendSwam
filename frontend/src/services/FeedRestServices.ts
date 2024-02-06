
import { Vue } from 'vue-class-component';
import axios, { Method } from 'axios';
import FeedDTO from '@/DTO/FeedDTO';
import { backendRestUrl, feedResourceUrl, backendTimeout } from '@/services/endpoints'
import { JsonStringifyReplacer } from '@/utils/JsonStringifyReplacer.ts';

export default class FeedRestServices extends Vue{
  fetchAllFeeds(): Promise<any>{
    return axios.request({
      url: feedResourceUrl,
      timeout: backendTimeout,
      method: 'get',
      baseURL: backendRestUrl,
      headers: {
          'Authorization': 'Bearer '+ this.$store.getters.getToken
      }
    });  
  }
  saveFeed(feedDto: FeedDTO): Promise<any>{
    let method: Method = "post";
    let specificResourceUrl = feedResourceUrl;
    if(feedDto.id !== null){
        method = "patch";
        specificResourceUrl = feedResourceUrl+"/"+ feedDto.id;
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
         data: JSON.stringify(feedDto,JsonStringifyReplacer),
       });
       
  }
  deleteFeed(feedId: number){
    return axios.request({
      url: feedResourceUrl+'/'+feedId,
      timeout: backendTimeout,
      method: 'delete',
      baseURL: backendRestUrl,
      headers: {
          'Authorization': 'Bearer '+ this.$store.getters.getToken
      }
    })
  }
  decodeFeedResponseError(error: string, statusCode: number): string{
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
        return "Esiste già un feed avente come URL quello fornito!";
    }
    return "Errore sconosciuto";
  }
  

}
