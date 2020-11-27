$(function() {
    loadData();
});

function getParameterByName(name) {
    var match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
    return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
};

function loadData(){
    $.get('/api/game_view/'+getParameterByName('gp'))
        .done(function(data) {
            let playerInfo;
            console.log(data.gamePlayers);
            if(data.gamePlayers[0].id == getParameterByName('gp'))
                playerInfo = [data.gamePlayers[0].player.email,data.gamePlayers[1].player.email];


            else
                playerInfo = [data.gamePlayers[1].player.email,data.gamePlayers[0].player.email];



            $('#playerInfo').text(playerInfo[0] + '(you) vs ' + playerInfo[1]);

            data.ships.forEach(function(shipPiece){
                console.log(shipPiece.type);
                //orientation(ships.location);
                shipPiece.locations.forEach(function(shipLocation){
                    switch (shipPiece.type){
                        case "carrier":
                            $('#'+shipLocation).addClass('ship-piece-carrier');
                            break;
                        case "corbeta":
                            $('#'+shipLocation).addClass('ship-piece-carrier');
                            break;
                        case "lancha":
                            $('#'+shipLocation).addClass('ship-piece-lancha');
                            break;
                        case "destructor":
                            $('#'+shipLocation).addClass('ship-piece-destructor');
                            break;
                        case "acorazado":
                            $('#'+shipLocation).addClass('ship-piece-acorazado');
                            break;
                        default:
                            $('#'+shipLocation).addClass('ship-piece');
                            break;
                    }

                })
            });
        })
        .fail(function( jqXHR, textStatus ) {
          alert( "Failed: " + textStatus );
        });
};

/*function orientation(location) {
    if (location)
};*/