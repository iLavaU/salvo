$(function() {
    loadData()
});

function updateView(data) {

    //Comente lo anterior por las dudas.

    /*let htmlList = data.map(function (game) {
        return  '<li>' + new Date(game.created).toLocaleString() + ' ' + game.gamePlayers.map(function(p) { return p.player.email}).join(', ')
        +'</li>';
    }).join('');*/

    //El codigo es muy similar, solo que en vez de una lista me arme una tabla.

    let htmlTable = data.map(function (game) {
        return   '<tr><td>' + game.id + '</td>' +
                 '<td>' + new Date(game.created).toLocaleString() + '</td>' +
                 game.gamePlayers.map(function(p) { return '<td>' + p.player.nombre + '</td>' + '<td>' + p.player.email + '</td>'}).join('') + '</tr>';
                 }).join('');
    document.getElementById("table-body").innerHTML = htmlTable;
  //document.getElementById("games-list").innerHTML = htmlList;
}

// load and display JSON sent by server for /players

function loadData() {
    $.get("/api/games")
        .done(function(data) {
          updateView(data);
        })
        .fail(function( jqXHR, textStatus ) {
          alert( "Failed: " + textStatus );
        });
}