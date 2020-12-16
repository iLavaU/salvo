function playLoginSound(){
    let loginSound = new Howl({
        src: ['./sounds/loginSound.mp3'],
        volume: 0.5,
       // loop: true
    })
    loginSound.play();
}