function setSalvoButtonAnim(){
    let salvoButton = document.getElementById('postSalvo');
    var salvo = bodymovin.loadAnimation({
        container: salvoButton,
        path: './img/salvo.json',
        autoplay: false,
        loop: false,
        renderer: 'svg',
        rendererSettings: {
            viewBoxSize: '40 60 450 190',
            viewBoxOnly: true

        }
    })

    salvoButton.addEventListener('mouseenter', () => {
        salvo.setDirection(1);
        salvo.play();
    })
    salvoButton.addEventListener('mouseleave', () => {
        salvo.setDirection(-1);
        salvo.play();
    })
}
