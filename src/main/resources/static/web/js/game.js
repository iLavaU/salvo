$(function() {
    loadShipsandPlayers();
    loadSalvoes();
});

function getParameterByName(name) {
    var match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
    return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
};

function loadShipsandPlayers(){
    $.get('/api/game_view/'+getParameterByName('gp'))
        .done(function(data) {
            let playerInfo;
            //console.log(data.gamePlayers);
            if(data.gamePlayers[0].id == getParameterByName('gp'))
                playerInfo = [data.gamePlayers[0].player.email,data.gamePlayers[1].player.email];


            else
                playerInfo = [data.gamePlayers[1].player.email,data.gamePlayers[0].player.email];



            $('#playerInfo').text(playerInfo[0] + '(you) vs ' + playerInfo[1]);

            data.ships.forEach(function(shipPiece){
                //console.log(shipPiece.type);
                //orientation(ships.location);
                shipPiece.locations.forEach(function(shipLocation){
                    switch (shipPiece.type){
                        case "carrier":
                            $('#table1 '+ '.'+shipLocation).addClass('ship-piece-carrier');
                            $('#table1 '+ '.'+shipLocation).addClass('ship-piece');
                            break;
                        case "corbeta":
                            $('#table1 '+ '.'+shipLocation).addClass('ship-piece-carrier');
                            $('#table1 '+ '.'+shipLocation).addClass('ship-piece');
                            break;
                        case "lancha":
                            $('#table1 '+ '.'+shipLocation).addClass('ship-piece-lancha');
                            $('#table1 '+ '.'+shipLocation).addClass('ship-piece');
                            break;
                        case "destructor":
                            $('#table1 '+ '.'+shipLocation).addClass('ship-piece-destructor');
                            $('#table1 '+ '.'+shipLocation).addClass('ship-piece');                      
                            break;
                        case "acorazado":
                            $('#table1 '+ '.'+shipLocation).addClass('ship-piece-acorazado');
                            $('#table1 '+ '.'+shipLocation).addClass('ship-piece');
                            break;
                        default:
                            $('#table1 '+ '.'+shipLocation).addClass('ship-piece-default');
                            $('#table1 '+ '.'+shipLocation).addClass('ship-piece');
                            break;
                    }
                })
            });
        })
        .fail(function( jqXHR, textStatus ) {
          alert( "Failed: " + textStatus );
        });
};

function loadSalvoes(){
    $.get('/api/game_view/'+getParameterByName('gp'))
        .done(function(data) {
            
            data.salvoes.forEach(function(salvoes) {
                if(salvoes.player == getParameterByName('gp')){
                    loadFriendlySalvoes(salvoes);
                } else {    
                    loadEnemySalvoes(salvoes);
                }
            })
        })
}

function loadFriendlySalvoes(salvo){
    console.log(salvo.locations)
    salvo.locations.forEach(function(salvoLocation) {
        $('#table2 '+ '.'+salvoLocation).addClass('salvo');
        $('#table2 '+ '.'+salvoLocation).append(salvo.turn);
        $('#table2 '+ '.'+salvoLocation).addClass('text-center');
    })
}

function loadEnemySalvoes(salvo){
    console.log(salvo.locations)
    salvo.locations.forEach(function(salvoLocation) {
        if ($('#table1 '+ '.'+salvoLocation).first().hasClass("ship-piece")) {
            $('#table1 '+ '.'+salvoLocation).addClass('ship-piece-hited');
            //$('#table1 '+ '.'+salvoLocation).append(salvo.turn);
        }else{
            $('#table1 '+ '.'+salvoLocation).addClass('salvo');
            //$('#table1 '+ '.'+salvoLocation).append(salvo.turn);
        }
    })
}

/*function orientation(location) {
    if (location)
};*/