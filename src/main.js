// Define our main state
var main = {

    preload: function () {
        this.load.image('mummy1', 'assets/mummy.png');
    },

    create: function () {
        this.add.sprite(0, 0, 'mummy1');
    }
};
