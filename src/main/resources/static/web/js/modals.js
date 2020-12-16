function displayModal(event){
    let windBody = document.getElementById('winBody');
    const animData = {
                container: winBody,
                renderer: 'svg',
                loop: false,
                autoplay: true,
                path: './js/victoria1.json'
     };
    if(event == "WON"){
        bodymovin.loadAnimation(animData);
        $("#winModal").modal('show');
    }else if(event == "LOST"){
        $("#lostModal").modal('show');
    }else {
        $("#tieModal").modal('show');
    }
}
