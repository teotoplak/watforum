/**
 * Created by teo on 12/9/16.
 */

var rated;
var ratedAvg;

var StarRating = {

/**
 * Initialize
 */
 init : function( x ) {
    rated = x;
    this.stars = document.querySelectorAll('#rating span');

    for (var i = 0; i < this.stars.length; i++) {
        this.stars[i].setAttribute('data-count', i);
        if( rated != -1) {
            if(rated > i) {
                this.stars[i].style.color = '#ff813a' ;
                this.stars[i].style.opacity = '1' ;
            }
        } else {
        this.stars[i].addEventListener('mouseenter', this.enterStarListener.bind(this));
        this.stars[i].addEventListener('click', this.clickedStar.bind(this));
        document.querySelector('#rating').addEventListener('mouseleave', this.leaveStarListener.bind(this));
        }
    }
    document.forms[0].elements["ratingInput"].value = rated;
},

/**
 * This method is fired when a user hovers over a single star
 * @param e
 */
    enterStarListener : function(e) {
    this.fillStarsUpToElement(e.target);
},

/**
 * This method is fired when the user leaves the #rating element, effectively removing all hover states.
 */
    leaveStarListener : function() {
    this.fillStarsUpToElement(null);
},

    clickedStar : function(e) {
       var el = e.target;
       var clickRated = el.getAttribute('data-count')
        clickRated++
        //removing listeners
        for (var i = 0; i < this.stars.length; i++) {
            var old_element =  this.stars[i];
            var new_element = old_element.cloneNode(true);
            old_element.parentNode.replaceChild(new_element, old_element);
        }

       this.init(clickRated)
    },


/**
 * Fill the star ratings up to a specific position.
 * @param el
 */
    fillStarsUpToElement : function(el) {
    // Remove all hover states:
    for (var i = 0; i < this.stars.length; i++) {
        if (el == null || this.stars[i].getAttribute('data-count') > el.getAttribute('data-count')) {
            this.stars[i].classList.remove('hover');
        } else {
            this.stars[i].classList.add('hover');
        }
    }
    }

};


var StarRatingAvg = {
    /**
     * Initialize
     */
    init : function( x ) {
        ratedAvg = x;
        this.stars = document.querySelectorAll('#ratingAvg span');
        for (var i = 0; i < this.stars.length; i++) {
                if(ratedAvg > i) {
                    this.stars[i].style.color = '#ff813a' ;
                    this.stars[i].style.opacity = '1' ;
                }
        }
    }
};


