function setSalvoAnim(){
    let salvoButton = document.getElementById('postSalvo');
    var salvo = bodymovin.loadAnimation({
        container: salvoButton,
        path: './img/salvo.json',
        autoplay: false,
        loop: false,
        renderer: 'svg',
        rendererSettings: {
            //This is for the salvov2.json:
            //viewBoxSize: '10 -5 400 190',
            viewBoxSize: '40 60 450 190',
            viewBoxOnly: true
        }
    })
    return salvo;
}
