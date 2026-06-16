// src/utils/gambitSmoothScroll.js
export default function GambitSmoothScroll(settings) {
  var key;
  var defaults = {
     'amount': 25,
     'speed': 900,
     'force': 3
  };

  if (typeof settings === 'undefined') {
     settings = {};
  }

  for (key in defaults) {
     if (!settings.hasOwnProperty(key)) {
        settings[key] = defaults[key];
     }
  }

  // Disable in mobile because we don't need smooth scrolling there
  if (navigator.userAgent.match(/Mobi|Android/)) {
     return;
  }

  this.settings = settings;
  this.startedAsTrackpad = false;
  this.start();
}

GambitSmoothScroll.prototype.parent_scroll = function(el) {
  var styles;
  while ('BODY' !== el.tagName && 'HTML' !== el.tagName) {
     styles = getComputedStyle(el);
     if (el.scrollHeight > el.clientHeight && 'hidden' !== styles.overflowY && 'visible' !== styles.overflowY) {
        return el;
     }
     el = el.parentNode;
  }
  return false;
};

GambitSmoothScroll.prototype.start = function() {
  document.addEventListener('DOMContentLoaded', function() {
     window.addEventListener('wheel', this.overrideScroll.bind(this), {passive: false});
  }.bind(this));
};

GambitSmoothScroll.prototype.stop = function() {
  if (typeof this.scrollInterval !== 'undefined') {
     this.startedAsTrackpad = false;
     clearInterval(this.scrollInterval);
     this.scrollInterval = undefined;
  }
};

GambitSmoothScroll.prototype.newScroll = function(isDown, scrollableElement, timestamp) {
  var multiplier;

  if (isDown !== this.isDown && typeof this.isDown !== 'undefined') {
     this.stop();
  }
  this.isDown = isDown;

  if (this.prevScrollableElement !== scrollableElement) {
     this.stop();
  }
  this.prevScrollableElement = scrollableElement;

  if (typeof this.scrollInterval === 'undefined') {
     this.startingSpeed = this.settings.amount;
     this.scrollInterval = setInterval(function() {
        var scrollBy = (this.isDown ? 1 : -1) * this.startingSpeed / this.settings.force;
        if (!scrollableElement) {
           window.scrollBy(0, scrollBy);
        } else {
           scrollableElement.scrollTop += scrollBy;
        }

        this.startingSpeed *= 1 - (900 / this.settings.speed) / 10;
        if (Math.abs(scrollBy) < 1) {
           this.stop();
        }
     }.bind(this), 16.666666667);
  } else {
     multiplier = 1 + (timestamp - this.prevTimestamp) / 40 * 0.7;
     this.startingSpeed = Math.max(this.startingSpeed * multiplier, this.settings.amount);
     this.startingSpeed = Math.min(this.startingSpeed, 300);
  }
  this.prevTimestamp = timestamp;
};

GambitSmoothScroll.prototype.overrideScroll = function(e) {
  var skipFirefoxMacCheck;
  var delta = e.wheelDelta ? -e.wheelDelta / 120 : (e.detail || e.deltaY) / 3;
  var scrollPercentage, scrollableElement = this.parent_scroll(e.target);
  var isSafari = (function(p) {
     return '[object SafariRemoteNotification]' === p.toString();
  })(!window.safari || safari.pushNotification);

  if (isSafari && (Math.abs(delta) < 1)) {
     delta *= 10;
  }

  skipFirefoxMacCheck = false;
  if (typeof window.mozInnerScreenX !== 'undefined') {
     if (navigator.platform.indexOf('Mac') !== -1) {
        this.startedAsTrackpad = false;
        skipFirefoxMacCheck = true;
        if (e.deltaY === parseInt(e.deltaY, 10)) {
           this.startedAsTrackpad = true;
           return;
        }
     }
  }

  if (typeof this.trackpadTimeout !== 'undefined') {
     clearTimeout(this.trackpadTimeout);
     this.trackpadTimeout = undefined;
  }

  if ((Math.abs(delta) < 1 || this.startedAsTrackpad) && !skipFirefoxMacCheck) {
     this.trackpadTimeout = setTimeout(function() {
        this.startedAsTrackpad = false;
        this.trackpadTimeout = undefined;
     }.bind(this), 200);
     this.startedAsTrackpad = true;
     return true;
  }

  e = e || window.event;
  if (e.preventDefault) {
     e.preventDefault();
  }
  e.returnValue = false;

  if (scrollableElement) {
     scrollPercentage = (scrollableElement.scrollTop + scrollableElement.offsetHeight) / scrollableElement.scrollHeight * 100;
     if (scrollableElement.scrollTop <= 0 && delta < 0) {
        scrollableElement = null;
     } else if (scrollPercentage >= 100 && delta > 0) {
        scrollableElement = null;
     }
  }

  this.newScroll(delta > 0, scrollableElement, e.timeStamp);
};
