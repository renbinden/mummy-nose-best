// Define our main state
var main = {

    preload: function () {
        this.load.spritesheet('mummy-all', 'assets/mummy-all.png', 263, 417, 3);
        this.load.spritesheet('tools', 'assets/tools.png', 64, 64, 10);
    },

    create: function () {
        var currentStep = 0;
        var correctToolIndex = 0;

        var mummySprite = this.add.sprite(0, 0, 'mummy-all', 0);

        var tools = [];
        var game = this;

        function getRandomInt(min, max) {
  return Math.floor(Math.random() * (max - min)) + min;
}

        function shuffle(array) {
  var currentIndex = array.length, temporaryValue, randomIndex;

  // While there remain elements to shuffle...
  while (0 !== currentIndex) {

    // Pick a remaining element...
    randomIndex = Math.floor(Math.random() * currentIndex);
    currentIndex -= 1;

    // And swap it with the current element.
    temporaryValue = array[currentIndex];
    array[currentIndex] = array[randomIndex];
    array[randomIndex] = temporaryValue;
  }

  return array;
}

        function setupNextStep() {
            setTools(currentStep);
            mummySprite.frame = 0;
        }

        function listener() {
            if(this.index == correctToolIndex) {
                mummySprite.frame = 2;
                currentStep++;
            } else {
                currentStep = 0;
                mummySprite.frame = 1;
            }
            game.time.events.add(Phaser.Timer.SECOND, setupNextStep, this);
        }

        for(var i=0;i<3;i++)
        {
            tools[i] = this.add.sprite(300 + 70*i, 200, 'tools', 0);
            tools[i].inputEnabled = true;
            tools[i].events.onInputDown.add(listener,{index:i});
        }

        setTools(currentStep);

        function setTools(currentStep) {
            correctToolIndex = getRandomInt(0,2);

            var allTools = Array.apply(null, Array(10)).map(function (_, i) {return i;});
            allTools.splice(currentStep, 1);

            allTools = shuffle(allTools);



                    for(var i=0;i<3;i++)
        {
            if(i == correctToolIndex) {
                tools[i].frame = currentStep;
            } else {
                tools[i].frame = allTools.pop();
            }
        }
        }
    }
};
