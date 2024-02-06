<template>
    <div class="mt-2" style="text-align:left">
        <div><b>{{tagDto.name}}</b></div>
        <div class="mt-1">Id del tag: <b>{{tagDto.id}}</b></div>
         <div v-if="tagDto.active" class="mt-1 bg-green-600 w-min whitespace-nowrap text-white p-1 rounded  leading-none items-center"> Tag attivo &check; </div>
        <div v-else class=" bg-gray-300 mt-1 w-min whitespace-nowrap text-white p-1 rounded  leading-none items-center"> Tag non attivo </div> 
    </div>
    <div class="mb-2" style="text-align:right">
        <button @click="$emit('edit',componentId)"  class="bg-green-500 inline font-bold text-white mr-2 mt-1 px-2 py-1 transition duration-300 ease-in-out hover:bg-green-700 mr-1">
          Modifica
        </button>
        <button @click="$emit('delete',componentId)"  class="bg-green-500 inline font-bold text-white mr-2 mt-1 px-2 py-1 transition duration-300 ease-in-out hover:bg-green-700 mr-1">
          Elimina
        </button>
    </div>
    <hr>
</template>

<script lang="ts">
import { Options, Vue } from 'vue-class-component';
import TagDTO from '@/DTO/TagDTO';



@Options({
  props: {
    tagDto: Object,
    tagName: String,
    componentId: Number,
    assignedFeeds: Number,
    activeOrDisabled: Boolean,
  },
   emits: {
    delete: false,
    edit: false
  }
})



export default class Tag extends Vue{
    tagDto!: TagDTO;
    tagName!: string;
    componentId!: number;
    assignedFeeds!: number;
    activeOrDisabled!: boolean;
}
</script>