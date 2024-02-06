import { Vue } from 'vue-class-component';


export default class ResponsiveAdapter extends Vue{
    windowHeight = window.innerHeight;
    get newHeight(): number{
        return  this.windowHeight;  
    }
    created() {
        window.addEventListener("resize", this.myEventHandler);
    }
    unmounted() {
        window.removeEventListener("resize", this.myEventHandler);
    }
    myEventHandler(e){
        this.windowHeight = window.innerHeight;
    }
}