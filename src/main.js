// Define our main state
var main = {

    preload: function () {
        this.load.spritesheet('mummy-all', 'assets/mummy-all.png', 263, 417, 3);
        this.load.spritesheet('tools', 'assets/tools.png', 64, 64, 3);
    },

    create: function () {
        var mummySprite = this.add.sprite(0, 0, 'mummy-all', 0);

        var tools = [];

        function getRandomInt(min, max) {
  return Math.floor(Math.random() * (max - min)) + min;
}

        function listener() {
            if(this.index == getRandomInt(0,2)) {
                mummySprite.frame = 2;
            } else {
                mummySprite.frame = 1;
            }
        }

        for(var i=0;i<3;i++)
        {
            tools[i] = this.add.sprite(300 + 70*i, 200, 'tools', i);
            tools[i].inputEnabled = true;
            tools[i].events.onInputDown.add(listener,{index:i});
        }
    }
};
