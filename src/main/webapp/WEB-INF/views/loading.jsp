  <style>
    *{margin: 0; padding: 0;}
  h1{ font-weight: normal; text-align:center; margin: 30px 0; }
  a{ color: #000; text-decoration: none;}
  a:hover{ color: #E78B16; text-decoration: underline;-moz-transition: color .3s linear;-webkit-transition: color .3s linear; transition: color .3s linear;}


.loader{
margin-left:20px
}
.loading {
  position: relative;
  display: inline-block;
}
.loading .dot {
  position: absolute;
  opacity: 0;
  width: 20px;
  height: 20px;
  -webkit-transform: rotate(225deg);
  -moz-transform: rotate(225deg);
  -o-transform: rotate(225deg);
  -ms-transform: rotate(225deg);
  transform: rotate(225deg);
  -webkit-animation-name: loading;
  -moz-animation-name: loading;
  -ms-animation-name: loading;
  -o-animation-name: loading;
  animation-name: loading;
  -webkit-animation-iteration-count: infinite;
  -moz-animation-iteration-count: infinite;
  -ms-animation-iteration-count: infinite;
  -o-animation-iteration-count: infinite;
  animation-iteration-count: infinite;
  -o-animation-duration: 5.28s;
  -moz-animation-duration: 5.28s;
  -webkit-animation-duration: 5.28s;
  animation-duration: 5.28s;
}
.loading .dot:after {
  content: "";
  position: absolute;
  width: 3px;
  height: 3px;
  border-radius: 50%;
  background: gray;
}
.loading .dot:nth-child(2) {
  -webkit-animation-delay: .23s;
  -moz-animation-delay: .23s;
  -ms-animation-delay: .23s;
  -o-animation-delay: .23s;
  animation-delay: .23s;
}
.loading .dot:nth-child(3) {
  -webkit-animation-delay: .46s;
  -moz-animation-delay: .46s;
  -ms-animation-delay: .46s;
  -o-animation-delay: .46s;
  animation-delay: .46s;
}
.loading .dot:nth-child(4) {
  -webkit-animation-delay: .69s;
  -moz-animation-delay: .69s;
  -ms-animation-delay: .69s;
  -o-animation-delay: .69s;
  animation-delay: .69s;
}
.loading .dot:nth-child(5) {
  -webkit-animation-delay: .92s;
  -moz-animation-delay: .92s;
  -ms-animation-delay: .92s;
  -o-animation-delay: .92s;
  animation-delay: .92s;
}
@-webkit-keyframes loading {
  0% {
    -webkit-transform: rotate(225deg);
    opacity: 1;
    -webkit-animation-timing-function: ease-out;
  }
  8% {
    -webkit-transform: rotate(345deg);
    -webkit-animation-timing-function: linear;
  }
  30% {
    -webkit-transform: rotate(455deg);
    -webkit-animation-timing-function: ease-in-out;
  }
  40% {
    -webkit-transform: rotate(690deg);
    -webkit-animation-timing-function: linear;
  }
  60% {
    -webkit-transform: rotate(815deg);
    opacity: 1;
    -webkit-animation-timing-function: ease-out;
  }
  75% {
    -webkit-transform: rotate(965deg);
    -webkit-animation-timing-function: ease-out;
  }
  76% {
    opacity: 0;
  }
  100% {
    opacity: 0;
  }
}

@-moz-keyframes loading {
  0% {
    -moz-transform: rotate(225deg);
    opacity: 1;
    -moz-animation-timing-function: ease-out;
  }
  8% {
    -moz-transform: rotate(345deg);
    -moz-animation-timing-function: linear;
  }
  30% {
    -moz-transform: rotate(455deg);
    -moz-animation-timing-function: ease-in-out;
  }
  40% {
    -moz-transform: rotate(690deg);
    -moz-animation-timing-function: linear;
  }
  60% {
    -moz-transform: rotate(815deg);
    opacity: 1;
    -moz-animation-timing-function: ease-out;
  }
  75% {
    -moz-transform: rotate(965deg);
    -moz-animation-timing-function: ease-out;
  }
  76% {
    opacity: 0;
  }
  100% {
    opacity: 0;
  }
}

@keyframes loading {
  0% {
    transform: rotate(225deg);
    opacity: 1;
    animation-timing-function: ease-out;
  }
  8% {
    transform: rotate(345deg);
    animation-timing-function: linear;
  }
  30% {
    transform: rotate(455deg);
    animation-timing-function: ease-in-out;
  }
  40% {
    transform: rotate(690deg);
    animation-timing-function: linear;
  }
  60% {
    transform: rotate(815deg);
    opacity: 1;
    animation-timing-function: ease-out;
  }
  75% {
    transform: rotate(965deg);
    animation-timing-function: ease-out;
  }
  76% {
    opacity: 0;
  }
  100% {
    opacity: 0;
  }
}
</style>
<div class="loader">
  <div class="vmt-load">
    <div class="dot"></div>
    <div class="dot"></div>
    <div class="dot"></div>
    <div class="dot"></div>
    <div class="dot"></div>
  </div>
</div>  
