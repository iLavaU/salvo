function playLoginSound() {
    var loginSound = new Howl({
        src: ['./sounds/loginSound.mp3'],
        volume: 0.5,
    });
    loginSound.play();
}