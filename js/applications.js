$(function(){

});

$(function(){
  $('body').on('click',"#app-container table tbody tr td" , function() {
        window.location.href="dashBoard.jsp?"+"application="+this.innerHTML;
    });
});
