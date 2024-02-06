<template>
  <div class="news">
    <TagListController @selected="changeTag"/>
    <NewsController/>
  </div>
</template>

<script lang="ts">
import { Options, Vue } from 'vue-class-component';
import {ProvideReactive} from 'vue-property-decorator';
import TagListController from '@/controllers/TagListController.vue';
import NewsController from '@/controllers/NewsController.vue';

@Options({
  components: {
    TagListController,
    NewsController,
  },
})
export default class Home extends Vue {
  @ProvideReactive() selectedTag = 0;
  changeTag( tagSelected: number){
      this.selectedTag = tagSelected;
  }
}
</script>

