<template>
<div class="mainDiv min-h-screen h-screen md:overflow-auto lg:overflow-hidden" style="background-color:black">
<div class="container " style="min-width:100%; justify-content: center; text-align: center;">
    <div class="tag">
        <div class="min-w-screen w-screen" style="position: relative; left: -50%;">
            <br><br><br>
            <p class="lg:text-3xl md:text-sm">Assignment for <br> Software Architectures and Methodologies <br>
            Academic year : 2019/2020 <br><br></p>
            
            <b class="lg:text-4xl md:text-lg"> Vue.js + Typescript + Tailwind CSS, with elements from Material.io, Frontend </b><br>
            
            Made by: Edoardo Cagnes, Andrea Giorgi.<br>
            <br>
            <span  class="smallerFont" > Reference course:</span><br>
            <a class="smallerFont" href="https://e-l.unifi.it/course/view.php?id=12438">B024307 (B070) - Software Architectures and Methodologies</a>
            <br><br>
            <span  class=" smallerFont" > Reference material:</span><br>
            <a class="smallerFont" href="https://v3.vuejs.org/">Vue 3 - The Progressive JavaScript Framework </a><br>
            <a class="smallerFont" href="https://tailwindcss.com/">Tailwind CSS - Rapidly build modern websites without ever leaving your HTML </a><br> 
            <a class="smallerFont" href="https://material.io/" >Material Design - Build beautiful products, faster.</a>
            <br>
        
        </div>
    </div>

    <img id="watermark" style="display: inline;" src="@/assets/watermark.png">
    </div>
</div>

</template>

<script lang="ts">
import { Vue } from 'vue-class-component'
export default class About extends Vue{
  mounted(){
    // document.querySelector('body').style.backgroundColor = "black";
  }
 
}
</script>

<style scoped>


        .mainDiv {
            /*position: absolute;
            top:0;
            left:0;
            margin: 0;
            width: 100%;
            min-height: 100%;*/
            
            color: white;
            text-align: center;
            font-family: "Trebuchet MS",serif;
            font-size:24pt;
        }
        body{
          background-color: black;
        }

        h1 {
            position: absolute;
            width: 100%;
            z-index: 1;
            font-size: 1.5rem;
        }

        a {
            color: white;
        }
        a:hover{
            color: deepskyblue;
        }



        .container {
            position: center;
            min-width: 100%;
        }

        @media (max-width:961px) {
            #watermark {
                position: center;

                visibility: hidden;

            }

            .tag {
                position: absolute;
                left: 50%;
                top: 10%;
                font-weight: bold;
                font-size:15pt;
            }

            .biggerFont{
                font-size:22pt;
            }
            .smallerFont{
                font-size:10pt;
            }
            .littleText{
                font-size:12pt;
                font-weight: normal;
            }
        }
        @media (min-width: 962px){
            #watermark
            {
                position:center;
                padding-top: 60px;

            }
            .tag {
                position: absolute;
                left: 50%;
                top: 10%;
                font-weight: bold;

            }
            .biggerFont{
                font-size:25pt;
            }
            .smallerFont{
                font-size:16pt;
            }
            .littleText{
                font-size:12pt;
                font-weight: normal;
            }
        }
    </style>