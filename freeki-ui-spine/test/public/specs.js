

(function(/*! Stitch !*/) {
  if (!this.specs) {
    var modules = {}, cache = {}, require = function(name, root) {
      var path = expand(root, name), module = cache[path], fn;
      if (module) {
        return module.exports;
      } else if (fn = modules[path] || modules[path = expand(path, './index')]) {
        module = {id: path, exports: {}};
        try {
          cache[path] = module;
          fn(module.exports, function(name) {
            return require(name, dirname(path));
          }, module);
          return module.exports;
        } catch (err) {
          delete cache[path];
          throw err;
        }
      } else {
        throw 'module \'' + name + '\' not found';
      }
    }, expand = function(root, name) {
      var results = [], parts, part;
      if (/^\.\.?(\/|$)/.test(name)) {
        parts = [root, name].join('/').split('/');
      } else {
        parts = name.split('/');
      }
      for (var i = 0, length = parts.length; i < length; i++) {
        part = parts[i];
        if (part == '..') {
          results.pop();
        } else if (part != '.' && part != '') {
          results.push(part);
        }
      }
      return results.join('/');
    }, dirname = function(path) {
      return path.split('/').slice(0, -1).join('/');
    };
    this.specs = function(name) {
      return require(name, '');
    }
    this.specs.define = function(bundle) {
      for (var key in bundle)
        modules[key] = bundle[key];
    };
    this.specs.modules = modules;
    this.specs.cache   = cache;
  }
  return this.specs.define;
}).call(this)({
  "controllers/pages": function(exports, require, module) {(function() {
  var require;

  require = window.require;

  describe('Pages', function() {
    var Pages;
    Pages = require('controllers/pages');
    return it('can noop', function() {});
  });

}).call(this);
}, "controllers/pages_display": function(exports, require, module) {(function() {
  var require;

  require = window.require;

  describe('PagesDisplay', function() {
    var PagesDisplay;
    PagesDisplay = require('controllers/pagesdisplay');
    return it('can noop', function() {});
  });

}).call(this);
}, "controllers/pages_index": function(exports, require, module) {(function() {
  var require;

  require = window.require;

  describe('PagesIndex', function() {
    var PagesIndex;
    PagesIndex = require('controllers/pagesindex');
    return it('can noop', function() {});
  });

}).call(this);
}, "models/page": function(exports, require, module) {(function() {
  var require;

  require = window.require;

  describe('Page', function() {
    var Page;
    Page = require('models/page');
    return it('can noop', function() {});
  });

}).call(this);
}
});

require('lib/setup'); for (var key in specs.modules) specs(key);