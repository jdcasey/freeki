(function() {
  if (!this.require) {
    var e = {},
        t = {},
        n = function(s, o) {
        var u = r(o, s),
            a = t[u],
            f;
        if (a) return a.exports;
        if (!(f = e[u] || e[u = r(u, "./index")])) throw "module '" + s + "' not found";
        a = {
          id: u,
          exports: {}
        };
        try {
          return t[u] = a, f(a.exports, function(e) {
            return n(e, i(u))
          }, a), a.exports
        } catch (l) {
          throw delete t[u], l
        }
        },
        r = function(e, t) {
        var n = [],
            r, i;
        /^\.\.?(\/|$)/.test(t) ? r = [e, t].join("/").split("/") : r = t.split("/");
        for (var s = 0, o = r.length; s < o; s++) i = r[s], i == ".." ? n.pop() : i != "." && i != "" && n.push(i);
        return n.join("/")
        },
        i = function(e) {
        return e.split("/").slice(0, -1).join("/")
        };
    this.require = function(e) {
      return n(e, "")
    }, this.require.define = function(t) {
      for (var n in t) e[n] = t[n]
    }, this.require.modules = e, this.require.cache = t
  }
  return this.require.define
}).call(this)({
  "es5-shimify/index": function(e, t, n) {
    (function(e) {
      typeof define == "function" ? define(function() {
        e()
      }) : e()
    })(function(e) {
      if (!Function.prototype.bind) {
        var t = Array.prototype.slice;
        Function.prototype.bind = function(n) {
          function s() {
            if (this instanceof s) {
              var e = Object.create(r.prototype);
              return r.apply(e, i.concat(t.call(arguments))), e
            }
            return r.call.apply(r, i.concat(t.call(arguments)))
          }
          var r = this;
          if (typeof r.apply != "function" || typeof r.call != "function") return new TypeError;
          var i = t.call(arguments);
          return s.length = typeof r == "function" ? Math.max(r.length - i.length, 0) : 0, s
        }
      }
      var n = Function.prototype.call,
          r = Array.prototype,
          i = Object.prototype,
          s = n.bind(i.hasOwnProperty),
          o, u, a, f, l;
      if (l = s(i, "__defineGetter__")) o = n.bind(i.__defineGetter__), u = n.bind(i.__defineSetter__), a = n.bind(i.__lookupGetter__), f = n.bind(i.__lookupSetter__);
      Array.isArray || (Array.isArray = function(t) {
        return Object.prototype.toString.call(t) === "[object Array]"
      }), Array.prototype.forEach || (Array.prototype.forEach = function(t, n) {
        var r = +this.length;
        for (var i = 0; i < r; i++) i in this && t.call(n, this[i], i, this)
      }), Array.prototype.map || (Array.prototype.map = function(t) {
        var n = +this.length;
        if (typeof t != "function") throw new TypeError;
        var r = new Array(n),
            i = arguments[1];
        for (var s = 0; s < n; s++) s in this && (r[s] = t.call(i, this[s], s, this));
        return r
      }), Array.prototype.filter || (Array.prototype.filter = function(t) {
        var n = [],
            r = arguments[1];
        for (var i = 0; i < this.length; i++) t.call(r, this[i]) && n.push(this[i]);
        return n
      }), Array.prototype.every || (Array.prototype.every = function(t) {
        var n = arguments[1];
        for (var r = 0; r < this.length; r++) if (!t.call(n, this[r])) return !1;
        return !0
      }), Array.prototype.some || (Array.prototype.some = function(t) {
        var n = arguments[1];
        for (var r = 0; r < this.length; r++) if (t.call(n, this[r])) return !0;
        return !1
      }), Array.prototype.reduce || (Array.prototype.reduce = function(t) {
        var n = +this.length;
        if (typeof t != "function") throw new TypeError;
        if (n === 0 && arguments.length === 1) throw new TypeError;
        var r = 0;
        if (arguments.length >= 2) var i = arguments[1];
        else do {
          if (r in this) {
            i = this[r++];
            break
          }
          if (++r >= n) throw new TypeError
        } while (!0);
        for (; r < n; r++) r in this && (i = t.call(null, i, this[r], r, this));
        return i
      }), Array.prototype.reduceRight || (Array.prototype.reduceRight = function(t) {
        var n = +this.length;
        if (typeof t != "function") throw new TypeError;
        if (n === 0 && arguments.length === 1) throw new TypeError;
        var r, i = n - 1;
        if (arguments.length >= 2) r = arguments[1];
        else do {
          if (i in this) {
            r = this[i--];
            break
          }
          if (--i < 0) throw new TypeError
        } while (!0);
        for (; i >= 0; i--) i in this && (r = t.call(null, r, this[i], i, this));
        return r
      }), Array.prototype.indexOf || (Array.prototype.indexOf = function(t) {
        var n = this.length;
        if (!n) return -1;
        var r = arguments[1] || 0;
        if (r >= n) return -1;
        r < 0 && (r += n);
        for (; r < n; r++) {
          if (!(r in this)) continue;
          if (t === this[r]) return r
        }
        return -1
      }), Array.prototype.lastIndexOf || (Array.prototype.lastIndexOf = function(t) {
        var n = this.length;
        if (!n) return -1;
        var r = arguments[1] || n;
        r < 0 && (r += n), r = Math.min(r, n - 1);
        for (; r >= 0; r--) {
          if (!(r in this)) continue;
          if (t === this[r]) return r
        }
        return -1
      }), Object.getPrototypeOf || (Object.getPrototypeOf = function(t) {
        return t.__proto__ || t.constructor.prototype
      });
      if (!Object.getOwnPropertyDescriptor) {
        var c = "Object.getOwnPropertyDescriptor called on a non-object: ";
        Object.getOwnPropertyDescriptor = function(n, r) {
          if (typeof n != "object" && typeof n != "function" || n === null) throw new TypeError(c + n);
          if (!s(n, r)) return e;
          var o, u, h;
          o = {
            enumerable: !0,
            configurable: !0
          };
          if (l) {
            var p = n.__proto__;
            n.__proto__ = i;
            var u = a(n, r),
                h = f(n, r);
            n.__proto__ = p;
            if (u || h) return u && (o.get = u), h && (o.set = h), o
          }
          return o.value = n[r], o
        }
      }
      Object.getOwnPropertyNames || (Object.getOwnPropertyNames = function(t) {
        return Object.keys(t)
      }), Object.create || (Object.create = function(t, n) {
        var r;
        if (t === null) r = {
          __proto__: null
        };
        else {
          if (typeof t != "object") throw new TypeError("typeof prototype[" + typeof t + "] != 'object'");
          var i = function() {};
          i.prototype = t, r = new i, r.__proto__ = t
        }
        return typeof n != "undefined" && Object.defineProperties(r, n), r
      });
      if (!Object.defineProperty) {
        var h = "Property description must be an object: ",
            p = "Object.defineProperty called on non-object: ",
            d = "getters & setters can not be defined on this javascript engine";
        Object.defineProperty = function(t, n, r) {
          if (typeof t != "object" && typeof t != "function") throw new TypeError(p + t);
          if (typeof r != "object" || r === null) throw new TypeError(h + r);
          if (s(r, "value")) if (l && (a(t, n) || f(t, n))) {
            var c = t.__proto__;
            t.__proto__ = i, delete t[n], t[n] = r.value, t.__proto__ = c
          } else t[n] = r.value;
          else {
            if (!l) throw new TypeError(d);
            s(r, "get") && o(t, n, r.get), s(r, "set") && u(t, n, r.set)
          }
          return t
        }
      }
      Object.defineProperties || (Object.defineProperties = function(t, n) {
        for (var r in n) s(n, r) && Object.defineProperty(t, r, n[r]);
        return t
      }), Object.seal || (Object.seal = function(t) {
        return t
      }), Object.freeze || (Object.freeze = function(t) {
        return t
      });
      try {
        Object.freeze(function() {})
      } catch (v) {
        Object.freeze = function(t) {
          return function(n) {
            return typeof n == "function" ? n : t(n)
          }
        }(Object.freeze)
      }
      Object.preventExtensions || (Object.preventExtensions = function(t) {
        return t
      }), Object.isSealed || (Object.isSealed = function(t) {
        return !1
      }), Object.isFrozen || (Object.isFrozen = function(t) {
        return !1
      }), Object.isExtensible || (Object.isExtensible = function(t) {
        return !0
      });
      if (!Object.keys) {
        var m = !0,
            g = ["toString", "toLocaleString", "valueOf", "hasOwnProperty", "isPrototypeOf", "propertyIsEnumerable", "constructor"],
            y = g.length;
        for (var b in {
          toString: null
        }) m = !1;
        Object.keys = function S(e) {
          if (typeof e != "object" && typeof e != "function" || e === null) throw new TypeError("Object.keys called on a non-object");
          var S = [];
          for (var t in e) s(e, t) && S.push(t);
          if (m) for (var n = 0, r = y; n < r; n++) {
            var i = g[n];
            s(e, i) && S.push(i)
          }
          return S
        }
      }
      Date.prototype.toISOString || (Date.prototype.toISOString = function() {
        return this.getUTCFullYear() + "-" + (this.getUTCMonth() + 1) + "-" + this.getUTCDate() + "T" + this.getUTCHours() + ":" + this.getUTCMinutes() + ":" + this.getUTCSeconds() + "Z"
      }), Date.now || (Date.now = function() {
        return (new Date).getTime()
      }), Date.prototype.toJSON || (Date.prototype.toJSON = function(t) {
        if (typeof this.toISOString != "function") throw new TypeError;
        return this.toISOString()
      }), isNaN(Date.parse("T00:00")) && (Date = function(t) {
        var n = function(e, r, i, s, o, u, a) {
            var f = arguments.length;
            if (this instanceof t) {
              var l = f === 1 && String(e) === e ? new t(n.parse(e)) : f >= 7 ? new t(e, r, i, s, o, u, a) : f >= 6 ? new t(e, r, i, s, o, u) : f >= 5 ? new t(e, r, i, s, o) : f >= 4 ? new t(e, r, i, s) : f >= 3 ? new t(e, r, i) : f >= 2 ? new t(e, r) : f >= 1 ? new t(e) : new t;
              return l.constructor = n, l
            }
            return t.apply(this, arguments)
            },
            r = new RegExp("^(?:((?:[+-]\\d\\d)?\\d\\d\\d\\d)(?:-(\\d\\d)(?:-(\\d\\d))?)?)?(?:T(\\d\\d):(\\d\\d)(?::(\\d\\d)(?:\\.(\\d\\d\\d))?)?)?(?:Z|([+-])(\\d\\d):(\\d\\d))?$");
        for (var i in t) n[i] = t[i];
        return n.now = t.now, n.UTC = t.UTC, n.prototype = t.prototype, n.prototype.constructor = n, n.parse = function(i) {
          var s = r.exec(i);
          if (s) {
            s.shift();
            var o = s[0] === e;
            for (var u = 0; u < 10; u++) {
              if (u === 7) continue;
              s[u] = +(s[u] || (u < 3 ? 1 : 0)), u === 1 && s[u]--
            }
            if (o) return ((s[3] * 60 + s[4]) * 60 + s[5]) * 1e3 + s[6];
            var a = (s[8] * 60 + s[9]) * 60 * 1e3;
            return s[6] === "-" && (a = -a), t.UTC.apply(this, s.slice(0, 7)) + a
          }
          return t.parse.apply(this, arguments)
        }, n
      }(Date));
      if (!String.prototype.trim) {
        var w = /^\s\s*/,
            E = /\s\s*$/;
        String.prototype.trim = function() {
          return String(this).replace(w, "").replace(E, "")
        }
      }
    })
  },
  "json2ify/index": function(exports, require, module) {
    typeof JSON != "object" && (JSON = {}), function() {
      "use strict";

      function f(e) {
        return e < 10 ? "0" + e : e
      }
      function quote(e) {
        return escapable.lastIndex = 0, escapable.test(e) ? '"' + e.replace(escapable, function(e) {
          var t = meta[e];
          return typeof t == "string" ? t : "\\u" + ("0000" + e.charCodeAt(0).toString(16)).slice(-4)
        }) + '"' : '"' + e + '"'
      }
      function str(e, t) {
        var n, r, i, s, o = gap,
            u, a = t[e];
        a && typeof a == "object" && typeof a.toJSON == "function" && (a = a.toJSON(e)), typeof rep == "function" && (a = rep.call(t, e, a));
        switch (typeof a) {
        case "string":
          return quote(a);
        case "number":
          return isFinite(a) ? String(a) : "null";
        case "boolean":
        case "null":
          return String(a);
        case "object":
          if (!a) return "null";
          gap += indent, u = [];
          if (Object.prototype.toString.apply(a) === "[object Array]") {
            s = a.length;
            for (n = 0; n < s; n += 1) u[n] = str(n, a) || "null";
            return i = u.length === 0 ? "[]" : gap ? "[\n" + gap + u.join(",\n" + gap) + "\n" + o + "]" : "[" + u.join(",") + "]", gap = o, i
          }
          if (rep && typeof rep == "object") {
            s = rep.length;
            for (n = 0; n < s; n += 1) typeof rep[n] == "string" && (r = rep[n], i = str(r, a), i && u.push(quote(r) + (gap ? ": " : ":") + i))
          } else for (r in a) Object.prototype.hasOwnProperty.call(a, r) && (i = str(r, a), i && u.push(quote(r) + (gap ? ": " : ":") + i));
          return i = u.length === 0 ? "{}" : gap ? "{\n" + gap + u.join(",\n" + gap) + "\n" + o + "}" : "{" + u.join(",") + "}", gap = o, i
        }
      }
      typeof Date.prototype.toJSON != "function" && (Date.prototype.toJSON = function(e) {
        return isFinite(this.valueOf()) ? this.getUTCFullYear() + "-" + f(this.getUTCMonth() + 1) + "-" + f(this.getUTCDate()) + "T" + f(this.getUTCHours()) + ":" + f(this.getUTCMinutes()) + ":" + f(this.getUTCSeconds()) + "Z" : null
      }, String.prototype.toJSON = Number.prototype.toJSON = Boolean.prototype.toJSON = function(e) {
        return this.valueOf()
      });
      var cx = /[\u0000\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,
          escapable = /[\\\"\x00-\x1f\x7f-\x9f\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,
          gap, indent, meta = {
          "\b": "\\b",
          "	": "\\t",
          "\n": "\\n",
          "\f": "\\f",
          "\r": "\\r",
          '"': '\\"',
          "\\": "\\\\"
          },
          rep;
      typeof JSON.stringify != "function" && (JSON.stringify = function(e, t, n) {
        var r;
        gap = "", indent = "";
        if (typeof n == "number") for (r = 0; r < n; r += 1) indent += " ";
        else typeof n == "string" && (indent = n);
        rep = t;
        if (!t || typeof t == "function" || typeof t == "object" && typeof t.length == "number") return str("", {
          "": e
        });
        throw new Error("JSON.stringify")
      }), typeof JSON.parse != "function" && (JSON.parse = function(text, reviver) {
        function walk(e, t) {
          var n, r, i = e[t];
          if (i && typeof i == "object") for (n in i) Object.prototype.hasOwnProperty.call(i, n) && (r = walk(i, n), r !== undefined ? i[n] = r : delete i[n]);
          return reviver.call(e, t, i)
        }
        var j;
        text = String(text), cx.lastIndex = 0, cx.test(text) && (text = text.replace(cx, function(e) {
          return "\\u" + ("0000" + e.charCodeAt(0).toString(16)).slice(-4)
        }));
        if (/^[\],:{}\s]*$/.test(text.replace(/\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g, "@").replace(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g, "]").replace(/(?:^|:|,)(?:\s*\[)+/g, ""))) return j = eval("(" + text + ")"), typeof reviver == "function" ? walk({
          "": j
        }, "") : j;
        throw new SyntaxError("JSON.parse")
      })
    }()
  },
  "jqueryify/index": function(e, t, n) {
    (function(e, t) {
      function u(e) {
        var t = o[e] = {},
            n, r;
        e = e.split(/\s+/);
        for (n = 0, r = e.length; n < r; n++) t[e[n]] = !0;
        return t
      }
      function c(e, n, r) {
        if (r === t && e.nodeType === 1) {
          var i = "data-" + n.replace(l, "-$1").toLowerCase();
          r = e.getAttribute(i);
          if (typeof r == "string") {
            try {
              r = r === "true" ? !0 : r === "false" ? !1 : r === "null" ? null : s.isNumeric(r) ? parseFloat(r) : f.test(r) ? s.parseJSON(r) : r
            } catch (o) {}
            s.data(e, n, r)
          } else r = t
        }
        return r
      }
      function h(e) {
        for (var t in e) {
          if (t === "data" && s.isEmptyObject(e[t])) continue;
          if (t !== "toJSON") return !1
        }
        return !0
      }
      function p(e, t, n) {
        var r = t + "defer",
            i = t + "queue",
            o = t + "mark",
            u = s._data(e, r);
        u && (n === "queue" || !s._data(e, i)) && (n === "mark" || !s._data(e, o)) && setTimeout(function() {
          !s._data(e, i) && !s._data(e, o) && (s.removeData(e, r, !0), u.fire())
        }, 0)
      }
      function H() {
        return !1
      }
      function B() {
        return !0
      }
      function W(e) {
        return !e || !e.parentNode || e.parentNode.nodeType === 11
      }
      function X(e, t, n) {
        t = t || 0;
        if (s.isFunction(t)) return s.grep(e, function(e, r) {
          var i = !! t.call(e, r, e);
          return i === n
        });
        if (t.nodeType) return s.grep(e, function(e, r) {
          return e === t === n
        });
        if (typeof t == "string") {
          var r = s.grep(e, function(e) {
            return e.nodeType === 1
          });
          if (q.test(t)) return s.filter(t, r, !n);
          t = s.filter(t, r)
        }
        return s.grep(e, function(e, r) {
          return s.inArray(e, t) >= 0 === n
        })
      }
      function V(e) {
        var t = $.split("|"),
            n = e.createDocumentFragment();
        if (n.createElement) while (t.length) n.createElement(t.pop());
        return n
      }
      function at(e, t) {
        return s.nodeName(e, "table") ? e.getElementsByTagName("tbody")[0] || e.appendChild(e.ownerDocument.createElement("tbody")) : e
      }
      function ft(e, t) {
        if (t.nodeType !== 1 || !s.hasData(e)) return;
        var n, r, i, o = s._data(e),
            u = s._data(t, o),
            a = o.events;
        if (a) {
          delete u.handle, u.events = {};
          for (n in a) for (r = 0, i = a[n].length; r < i; r++) s.event.add(t, n + (a[n][r].namespace ? "." : "") + a[n][r].namespace, a[n][r], a[n][r].data)
        }
        u.data && (u.data = s.extend({}, u.data))
      }
      function lt(e, t) {
        var n;
        if (t.nodeType !== 1) return;
        t.clearAttributes && t.clearAttributes(), t.mergeAttributes && t.mergeAttributes(e), n = t.nodeName.toLowerCase();
        if (n === "object") t.outerHTML = e.outerHTML;
        else if (n !== "input" || e.type !== "checkbox" && e.type !== "radio") {
          if (n === "option") t.selected = e.defaultSelected;
          else if (n === "input" || n === "textarea") t.defaultValue = e.defaultValue
        } else e.checked && (t.defaultChecked = t.checked = e.checked), t.value !== e.value && (t.value = e.value);
        t.removeAttribute(s.expando)
      }
      function ct(e) {
        return typeof e.getElementsByTagName != "undefined" ? e.getElementsByTagName("*") : typeof e.querySelectorAll != "undefined" ? e.querySelectorAll("*") : []
      }
      function ht(e) {
        if (e.type === "checkbox" || e.type === "radio") e.defaultChecked = e.checked
      }
      function pt(e) {
        var t = (e.nodeName || "").toLowerCase();
        t === "input" ? ht(e) : t !== "script" && typeof e.getElementsByTagName != "undefined" && s.grep(e.getElementsByTagName("input"), ht)
      }
      function dt(e) {
        var t = n.createElement("div");
        return ut.appendChild(t), t.innerHTML = e.outerHTML, t.firstChild
      }
      function vt(e, t) {
        t.src ? s.ajax({
          url: t.src,
          async: !1,
          dataType: "script"
        }) : s.globalEval((t.text || t.textContent || t.innerHTML || "").replace(st, "/*$0*/")), t.parentNode && t.parentNode.removeChild(t)
      }
      function Lt(e, t, n) {
        var r = t === "width" ? e.offsetWidth : e.offsetHeight,
            i = t === "width" ? xt : Tt,
            o = 0,
            u = i.length;
        if (r > 0) {
          if (n !== "border") for (; o < u; o++) n || (r -= parseFloat(s.css(e, "padding" + i[o])) || 0), n === "margin" ? r += parseFloat(s.css(e, n + i[o])) || 0 : r -= parseFloat(s.css(e, "border" + i[o] + "Width")) || 0;
          return r + "px"
        }
        r = Nt(e, t, t);
        if (r < 0 || r == null) r = e.style[t] || 0;
        r = parseFloat(r) || 0;
        if (n) for (; o < u; o++) r += parseFloat(s.css(e, "padding" + i[o])) || 0, n !== "padding" && (r += parseFloat(s.css(e, "border" + i[o] + "Width")) || 0), n === "margin" && (r += parseFloat(s.css(e, n + i[o])) || 0);
        return r + "px"
      }
      function Gt(e) {
        return function(t, n) {
          typeof t != "string" && (n = t, t = "*");
          if (s.isFunction(n)) {
            var r = t.toLowerCase().split(Rt),
                i = 0,
                o = r.length,
                u, a, f;
            for (; i < o; i++) u = r[i], f = /^\+/.test(u), f && (u = u.substr(1) || "*"), a = e[u] = e[u] || [], a[f ? "unshift" : "push"](n)
          }
        }
      }
      function Yt(e, n, r, i, s, o) {
        s = s || n.dataTypes[0], o = o || {}, o[s] = !0;
        var u = e[s],
            a = 0,
            f = u ? u.length : 0,
            l = e === Xt,
            c;
        for (; a < f && (l || !c); a++) c = u[a](n, r, i), typeof c == "string" && (!l || o[c] ? c = t : (n.dataTypes.unshift(c), c = Yt(e, n, r, i, c, o)));
        return (l || !c) && !o["*"] && (c = Yt(e, n, r, i, "*", o)), c
      }
      function Zt(e, n) {
        var r, i, o = s.ajaxSettings.flatOptions || {};
        for (r in n) n[r] !== t && ((o[r] ? e : i || (i = {}))[r] = n[r]);
        i && s.extend(!0, e, i)
      }
      function en(e, t, n, r) {
        if (s.isArray(t)) s.each(t, function(t, i) {
          n || Ot.test(e) ? r(e, i) : en(e + "[" + (typeof i == "object" || s.isArray(i) ? t : "") + "]", i, n, r)
        });
        else if (!n && t != null && typeof t == "object") for (var i in t) en(e + "[" + i + "]", t[i], n, r);
        else r(e, t)
      }
      function tn(e, n, r) {
        var i = e.contents,
            s = e.dataTypes,
            o = e.responseFields,
            u, a, f, l;
        for (a in o) a in r && (n[o[a]] = r[a]);
        while (s[0] === "*") s.shift(), u === t && (u = e.mimeType || n.getResponseHeader("content-type"));
        if (u) for (a in i) if (i[a] && i[a].test(u)) {
          s.unshift(a);
          break
        }
        if (s[0] in r) f = s[0];
        else {
          for (a in r) {
            if (!s[0] || e.converters[a + " " + s[0]]) {
              f = a;
              break
            }
            l || (l = a)
          }
          f = f || l
        }
        if (f) return f !== s[0] && s.unshift(f), r[f]
      }
      function nn(e, n) {
        e.dataFilter && (n = e.dataFilter(n, e.dataType));
        var r = e.dataTypes,
            i = {},
            o, u, a = r.length,
            f, l = r[0],
            c, h, p, d, v;
        for (o = 1; o < a; o++) {
          if (o === 1) for (u in e.converters) typeof u == "string" && (i[u.toLowerCase()] = e.converters[u]);
          c = l, l = r[o];
          if (l === "*") l = c;
          else if (c !== "*" && c !== l) {
            h = c + " " + l, p = i[h] || i["* " + l];
            if (!p) {
              v = t;
              for (d in i) {
                f = d.split(" ");
                if (f[0] === c || f[0] === "*") {
                  v = i[f[1] + " " + l];
                  if (v) {
                    d = i[d], d === !0 ? p = v : v === !0 && (p = d);
                    break
                  }
                }
              }
            }!p && !v && s.error("No conversion from " + h.replace(" ", " to ")), p !== !0 && (n = p ? p(n) : v(d(n)))
          }
        }
        return n
      }
      function fn() {
        try {
          return new e.XMLHttpRequest
        } catch (t) {}
      }
      function ln() {
        try {
          return new e.ActiveXObject("Microsoft.XMLHTTP")
        } catch (t) {}
      }
      function bn() {
        return setTimeout(wn, 0), yn = s.now()
      }
      function wn() {
        yn = t
      }
      function En(e, t) {
        var n = {};
        return s.each(gn.concat.apply([], gn.slice(0, t)), function() {
          n[this] = e
        }), n
      }
      function Sn(e) {
        if (!cn[e]) {
          var t = n.body,
              r = s("<" + e + ">").appendTo(t),
              i = r.css("display");
          r.remove();
          if (i === "none" || i === "") {
            hn || (hn = n.createElement("iframe"), hn.frameBorder = hn.width = hn.height = 0), t.appendChild(hn);
            if (!pn || !hn.createElement) pn = (hn.contentWindow || hn.contentDocument).document, pn.write((n.compatMode === "CSS1Compat" ? "<!doctype html>" : "") + "<html><body>"), pn.close();
            r = pn.createElement(e), pn.body.appendChild(r), i = s.css(r, "display"), t.removeChild(hn)
          }
          cn[e] = i
        }
        return cn[e]
      }
      function Nn(e) {
        return s.isWindow(e) ? e : e.nodeType === 9 ? e.defaultView || e.parentWindow : !1
      }
      var n = e.document,
          r = e.navigator,
          i = e.location,
          s = function() {
          function H() {
            if (i.isReady) return;
            try {
              n.documentElement.doScroll("left")
            } catch (e) {
              setTimeout(H, 1);
              return
            }
            i.ready()
          }
          var i = function(e, t) {
              return new i.fn.init(e, t, u)
              },
              s = e.jQuery,
              o = e.$,
              u, a = /^(?:[^#<]*(<[\w\W]+>)[^>]*$|#([\w\-]*)$)/,
              f = /\S/,
              l = /^\s+/,
              c = /\s+$/,
              h = /^<(\w+)\s*\/?>(?:<\/\1>)?$/,
              p = /^[\],:{}\s]*$/,
              d = /\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g,
              v = /"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g,
              m = /(?:^|:|,)(?:\s*\[)+/g,
              g = /(webkit)[ \/]([\w.]+)/,
              y = /(opera)(?:.*version)?[ \/]([\w.]+)/,
              b = /(msie) ([\w.]+)/,
              w = /(mozilla)(?:.*? rv:([\w.]+))?/,
              E = /-([a-z]|[0-9])/ig,
              S = /^-ms-/,
              x = function(e, t) {
              return (t + "").toUpperCase()
              },
              T = r.userAgent,
              N, C, k, L = Object.prototype.toString,
              A = Object.prototype.hasOwnProperty,
              O = Array.prototype.push,
              M = Array.prototype.slice,
              _ = String.prototype.trim,
              D = Array.prototype.indexOf,
              P = {};
          return i.fn = i.prototype = {
            constructor: i,
            init: function(e, r, s) {
              var o, u, f, l;
              if (!e) return this;
              if (e.nodeType) return this.context = this[0] = e, this.length = 1, this;
              if (e === "body" && !r && n.body) return this.context = n, this[0] = n.body, this.selector = e, this.length = 1, this;
              if (typeof e == "string") {
                e.charAt(0) === "<" && e.charAt(e.length - 1) === ">" && e.length >= 3 ? o = [null, e, null] : o = a.exec(e);
                if (o && (o[1] || !r)) {
                  if (o[1]) return r = r instanceof i ? r[0] : r, l = r ? r.ownerDocument || r : n, f = h.exec(e), f ? i.isPlainObject(r) ? (e = [n.createElement(f[1])], i.fn.attr.call(e, r, !0)) : e = [l.createElement(f[1])] : (f = i.buildFragment([o[1]], [l]), e = (f.cacheable ? i.clone(f.fragment) : f.fragment).childNodes), i.merge(this, e);
                  u = n.getElementById(o[2]);
                  if (u && u.parentNode) {
                    if (u.id !== o[2]) return s.find(e);
                    this.length = 1, this[0] = u
                  }
                  return this.context = n, this.selector = e, this
                }
                return !r || r.jquery ? (r || s).find(e) : this.constructor(r).find(e)
              }
              return i.isFunction(e) ? s.ready(e) : (e.selector !== t && (this.selector = e.selector, this.context = e.context), i.makeArray(e, this))
            },
            selector: "",
            jquery: "1.7.1",
            length: 0,
            size: function() {
              return this.length
            },
            toArray: function() {
              return M.call(this, 0)
            },
            get: function(e) {
              return e == null ? this.toArray() : e < 0 ? this[this.length + e] : this[e]
            },
            pushStack: function(e, t, n) {
              var r = this.constructor();
              return i.isArray(e) ? O.apply(r, e) : i.merge(r, e), r.prevObject = this, r.context = this.context, t === "find" ? r.selector = this.selector + (this.selector ? " " : "") + n : t && (r.selector = this.selector + "." + t + "(" + n + ")"), r
            },
            each: function(e, t) {
              return i.each(this, e, t)
            },
            ready: function(e) {
              return i.bindReady(), C.add(e), this
            },
            eq: function(e) {
              return e = +e, e === -1 ? this.slice(e) : this.slice(e, e + 1)
            },
            first: function() {
              return this.eq(0)
            },
            last: function() {
              return this.eq(-1)
            },
            slice: function() {
              return this.pushStack(M.apply(this, arguments), "slice", M.call(arguments).join(","))
            },
            map: function(e) {
              return this.pushStack(i.map(this, function(t, n) {
                return e.call(t, n, t)
              }))
            },
            end: function() {
              return this.prevObject || this.constructor(null)
            },
            push: O,
            sort: [].sort,
            splice: [].splice
          }, i.fn.init.prototype = i.fn, i.extend = i.fn.extend = function() {
            var e, n, r, s, o, u, a = arguments[0] || {},
                f = 1,
                l = arguments.length,
                c = !1;
            typeof a == "boolean" && (c = a, a = arguments[1] || {}, f = 2), typeof a != "object" && !i.isFunction(a) && (a = {}), l === f && (a = this, --f);
            for (; f < l; f++) if ((e = arguments[f]) != null) for (n in e) {
              r = a[n], s = e[n];
              if (a === s) continue;
              c && s && (i.isPlainObject(s) || (o = i.isArray(s))) ? (o ? (o = !1, u = r && i.isArray(r) ? r : []) : u = r && i.isPlainObject(r) ? r : {}, a[n] = i.extend(c, u, s)) : s !== t && (a[n] = s)
            }
            return a
          }, i.extend({
            noConflict: function(t) {
              return e.$ === i && (e.$ = o), t && e.jQuery === i && (e.jQuery = s), i
            },
            isReady: !1,
            readyWait: 1,
            holdReady: function(e) {
              e ? i.readyWait++ : i.ready(!0)
            },
            ready: function(e) {
              if (e === !0 && !--i.readyWait || e !== !0 && !i.isReady) {
                if (!n.body) return setTimeout(i.ready, 1);
                i.isReady = !0;
                if (e !== !0 && --i.readyWait > 0) return;
                C.fireWith(n, [i]), i.fn.trigger && i(n).trigger("ready").off("ready")
              }
            },
            bindReady: function() {
              if (C) return;
              C = i.Callbacks("once memory");
              if (n.readyState === "complete") return setTimeout(i.ready, 1);
              if (n.addEventListener) n.addEventListener("DOMContentLoaded", k, !1), e.addEventListener("load", i.ready, !1);
              else if (n.attachEvent) {
                n.attachEvent("onreadystatechange", k), e.attachEvent("onload", i.ready);
                var t = !1;
                try {
                  t = e.frameElement == null
                } catch (r) {}
                n.documentElement.doScroll && t && H()
              }
            },
            isFunction: function(e) {
              return i.type(e) === "function"
            },
            isArray: Array.isArray ||
            function(e) {
              return i.type(e) === "array"
            },
            isWindow: function(e) {
              return e && typeof e == "object" && "setInterval" in e
            },
            isNumeric: function(e) {
              return !isNaN(parseFloat(e)) && isFinite(e)
            },
            type: function(e) {
              return e == null ? String(e) : P[L.call(e)] || "object"
            },
            isPlainObject: function(e) {
              if (!e || i.type(e) !== "object" || e.nodeType || i.isWindow(e)) return !1;
              try {
                if (e.constructor && !A.call(e, "constructor") && !A.call(e.constructor.prototype, "isPrototypeOf")) return !1
              } catch (n) {
                return !1
              }
              var r;
              for (r in e);
              return r === t || A.call(e, r)
            },
            isEmptyObject: function(e) {
              for (var t in e) return !1;
              return !0
            },
            error: function(e) {
              throw new Error(e)
            },
            parseJSON: function(t) {
              if (typeof t != "string" || !t) return null;
              t = i.trim(t);
              if (e.JSON && e.JSON.parse) return e.JSON.parse(t);
              if (p.test(t.replace(d, "@").replace(v, "]").replace(m, ""))) return (new Function("return " + t))();
              i.error("Invalid JSON: " + t)
            },
            parseXML: function(n) {
              var r, s;
              try {
                e.DOMParser ? (s = new DOMParser, r = s.parseFromString(n, "text/xml")) : (r = new ActiveXObject("Microsoft.XMLDOM"), r.async = "false", r.loadXML(n))
              } catch (o) {
                r = t
              }
              return (!r || !r.documentElement || r.getElementsByTagName("parsererror").length) && i.error("Invalid XML: " + n), r
            },
            noop: function() {},
            globalEval: function(t) {
              t && f.test(t) && (e.execScript ||
              function(t) {
                e.eval.call(e, t)
              })(t)
            },
            camelCase: function(e) {
              return e.replace(S, "ms-").replace(E, x)
            },
            nodeName: function(e, t) {
              return e.nodeName && e.nodeName.toUpperCase() === t.toUpperCase()
            },
            each: function(e, n, r) {
              var s, o = 0,
                  u = e.length,
                  a = u === t || i.isFunction(e);
              if (r) {
                if (a) {
                  for (s in e) if (n.apply(e[s], r) === !1) break
                } else for (; o < u;) if (n.apply(e[o++], r) === !1) break
              } else if (a) {
                for (s in e) if (n.call(e[s], s, e[s]) === !1) break
              } else for (; o < u;) if (n.call(e[o], o, e[o++]) === !1) break;
              return e
            },
            trim: _ ?
            function(e) {
              return e == null ? "" : _.call(e)
            } : function(e) {
              return e == null ? "" : e.toString().replace(l, "").replace(c, "")
            },
            makeArray: function(e, t) {
              var n = t || [];
              if (e != null) {
                var r = i.type(e);
                e.length == null || r === "string" || r === "function" || r === "regexp" || i.isWindow(e) ? O.call(n, e) : i.merge(n, e)
              }
              return n
            },
            inArray: function(e, t, n) {
              var r;
              if (t) {
                if (D) return D.call(t, e, n);
                r = t.length, n = n ? n < 0 ? Math.max(0, r + n) : n : 0;
                for (; n < r; n++) if (n in t && t[n] === e) return n
              }
              return -1
            },
            merge: function(e, n) {
              var r = e.length,
                  i = 0;
              if (typeof n.length == "number") for (var s = n.length; i < s; i++) e[r++] = n[i];
              else while (n[i] !== t) e[r++] = n[i++];
              return e.length = r, e
            },
            grep: function(e, t, n) {
              var r = [],
                  i;
              n = !! n;
              for (var s = 0, o = e.length; s < o; s++) i = !! t(e[s], s), n !== i && r.push(e[s]);
              return r
            },
            map: function(e, n, r) {
              var s, o, u = [],
                  a = 0,
                  f = e.length,
                  l = e instanceof i || f !== t && typeof f == "number" && (f > 0 && e[0] && e[f - 1] || f === 0 || i.isArray(e));
              if (l) for (; a < f; a++) s = n(e[a], a, r), s != null && (u[u.length] = s);
              else for (o in e) s = n(e[o], o, r), s != null && (u[u.length] = s);
              return u.concat.apply([], u)
            },
            guid: 1,
            proxy: function(e, n) {
              if (typeof n == "string") {
                var r = e[n];
                n = e, e = r
              }
              if (!i.isFunction(e)) return t;
              var s = M.call(arguments, 2),
                  o = function() {
                  return e.apply(n, s.concat(M.call(arguments)))
                  };
              return o.guid = e.guid = e.guid || o.guid || i.guid++, o
            },
            access: function(e, n, r, s, o, u) {
              var a = e.length;
              if (typeof n == "object") {
                for (var f in n) i.access(e, f, n[f], s, o, r);
                return e
              }
              if (r !== t) {
                s = !u && s && i.isFunction(r);
                for (var l = 0; l < a; l++) o(e[l], n, s ? r.call(e[l], l, o(e[l], n)) : r, u);
                return e
              }
              return a ? o(e[0], n) : t
            },
            now: function() {
              return (new Date).getTime()
            },
            uaMatch: function(e) {
              e = e.toLowerCase();
              var t = g.exec(e) || y.exec(e) || b.exec(e) || e.indexOf("compatible") < 0 && w.exec(e) || [];
              return {
                browser: t[1] || "",
                version: t[2] || "0"
              }
            },
            sub: function() {
              function e(t, n) {
                return new e.fn.init(t, n)
              }
              i.extend(!0, e, this), e.superclass = this, e.fn = e.prototype = this(), e.fn.constructor = e, e.sub = this.sub, e.fn.init = function(r, s) {
                return s && s instanceof i && !(s instanceof e) && (s = e(s)), i.fn.init.call(this, r, s, t)
              }, e.fn.init.prototype = e.fn;
              var t = e(n);
              return e
            },
            browser: {}
          }), i.each("Boolean Number String Function Array Date RegExp Object".split(" "), function(e, t) {
            P["[object " + t + "]"] = t.toLowerCase()
          }), N = i.uaMatch(T), N.browser && (i.browser[N.browser] = !0, i.browser.version = N.version), i.browser.webkit && (i.browser.safari = !0), f.test(" ") && (l = /^[\s\xA0]+/, c = /[\s\xA0]+$/), u = i(n), n.addEventListener ? k = function() {
            n.removeEventListener("DOMContentLoaded", k, !1), i.ready()
          } : n.attachEvent && (k = function() {
            n.readyState === "complete" && (n.detachEvent("onreadystatechange", k), i.ready())
          }), i
          }(),
          o = {};
      s.Callbacks = function(e) {
        e = e ? o[e] || u(e) : {};
        var n = [],
            r = [],
            i, a, f, l, c, h = function(t) {
            var r, i, o, u, a;
            for (r = 0, i = t.length; r < i; r++) o = t[r], u = s.type(o), u === "array" ? h(o) : u === "function" && (!e.unique || !d.has(o)) && n.push(o)
            },
            p = function(t, s) {
            s = s || [], i = !e.memory || [t, s], a = !0, c = f || 0, f = 0, l = n.length;
            for (; n && c < l; c++) if (n[c].apply(t, s) === !1 && e.stopOnFalse) {
              i = !0;
              break
            }
            a = !1, n && (e.once ? i === !0 ? d.disable() : n = [] : r && r.length && (i = r.shift(), d.fireWith(i[0], i[1])))
            },
            d = {
            add: function() {
              if (n) {
                var e = n.length;
                h(arguments), a ? l = n.length : i && i !== !0 && (f = e, p(i[0], i[1]))
              }
              return this
            },
            remove: function() {
              if (n) {
                var t = arguments,
                    r = 0,
                    i = t.length;
                for (; r < i; r++) for (var s = 0; s < n.length; s++) if (t[r] === n[s]) {
                  a && s <= l && (l--, s <= c && c--), n.splice(s--, 1);
                  if (e.unique) break
                }
              }
              return this
            },
            has: function(e) {
              if (n) {
                var t = 0,
                    r = n.length;
                for (; t < r; t++) if (e === n[t]) return !0
              }
              return !1
            },
            empty: function() {
              return n = [], this
            },
            disable: function() {
              return n = r = i = t, this
            },
            disabled: function() {
              return !n
            },
            lock: function() {
              return r = t, (!i || i === !0) && d.disable(), this
            },
            locked: function() {
              return !r
            },
            fireWith: function(t, n) {
              return r && (a ? e.once || r.push([t, n]) : (!e.once || !i) && p(t, n)), this
            },
            fire: function() {
              return d.fireWith(this, arguments), this
            },
            fired: function() {
              return !!i
            }
            };
        return d
      };
      var a = [].slice;
      s.extend({
        Deferred: function(e) {
          var t = s.Callbacks("once memory"),
              n = s.Callbacks("once memory"),
              r = s.Callbacks("memory"),
              i = "pending",
              o = {
              resolve: t,
              reject: n,
              notify: r
              },
              u = {
              done: t.add,
              fail: n.add,
              progress: r.add,
              state: function() {
                return i
              },
              isResolved: t.fired,
              isRejected: n.fired,
              then: function(e, t, n) {
                return a.done(e).fail(t).progress(n), this
              },
              always: function() {
                return a.done.apply(a, arguments).fail.apply(a, arguments), this
              },
              pipe: function(e, t, n) {
                return s.Deferred(function(r) {
                  s.each({
                    done: [e, "resolve"],
                    fail: [t, "reject"],
                    progress: [n, "notify"]
                  }, function(e, t) {
                    var n = t[0],
                        i = t[1],
                        o;
                    s.isFunction(n) ? a[e](function() {
                      o = n.apply(this, arguments), o && s.isFunction(o.promise) ? o.promise().then(r.resolve, r.reject, r.notify) : r[i + "With"](this === a ? r : this, [o])
                    }) : a[e](r[i])
                  })
                }).promise()
              },
              promise: function(e) {
                if (e == null) e = u;
                else for (var t in u) e[t] = u[t];
                return e
              }
              },
              a = u.promise({}),
              f;
          for (f in o) a[f] = o[f].fire, a[f + "With"] = o[f].fireWith;
          return a.done(function() {
            i = "resolved"
          }, n.disable, r.lock).fail(function() {
            i = "rejected"
          }, t.disable, r.lock), e && e.call(a, a), a
        },
        when: function(e) {
          function c(e) {
            return function(n) {
              t[e] = arguments.length > 1 ? a.call(arguments, 0) : n, --o || f.resolveWith(f, t)
            }
          }
          function h(e) {
            return function(t) {
              i[e] = arguments.length > 1 ? a.call(arguments, 0) : t, f.notifyWith(l, i)
            }
          }
          var t = a.call(arguments, 0),
              n = 0,
              r = t.length,
              i = new Array(r),
              o = r,
              u = r,
              f = r <= 1 && e && s.isFunction(e.promise) ? e : s.Deferred(),
              l = f.promise();
          if (r > 1) {
            for (; n < r; n++) t[n] && t[n].promise && s.isFunction(t[n].promise) ? t[n].promise().then(c(n), f.reject, h(n)) : --o;
            o || f.resolveWith(f, t)
          } else f !== e && f.resolveWith(f, r ? [e] : []);
          return l
        }
      }), s.support = function() {
        var t, r, i, o, u, a, f, l, c, h, p, d, v, m = n.createElement("div"),
            g = n.documentElement;
        m.setAttribute("className", "t"), m.innerHTML = "   <link/><table></table><a href='/a' style='top:1px;float:left;opacity:.55;'>a</a><input type='checkbox'/>", r = m.getElementsByTagName("*"), i = m.getElementsByTagName("a")[0];
        if (!r || !r.length || !i) return {};
        o = n.createElement("select"), u = o.appendChild(n.createElement("option")), a = m.getElementsByTagName("input")[0], t = {
          leadingWhitespace: m.firstChild.nodeType === 3,
          tbody: !m.getElementsByTagName("tbody").length,
          htmlSerialize: !! m.getElementsByTagName("link").length,
          style: /top/.test(i.getAttribute("style")),
          hrefNormalized: i.getAttribute("href") === "/a",
          opacity: /^0.55/.test(i.style.opacity),
          cssFloat: !! i.style.cssFloat,
          checkOn: a.value === "on",
          optSelected: u.selected,
          getSetAttribute: m.className !== "t",
          enctype: !! n.createElement("form").enctype,
          html5Clone: n.createElement("nav").cloneNode(!0).outerHTML !== "<:nav></:nav>",
          submitBubbles: !0,
          changeBubbles: !0,
          focusinBubbles: !1,
          deleteExpando: !0,
          noCloneEvent: !0,
          inlineBlockNeedsLayout: !1,
          shrinkWrapBlocks: !1,
          reliableMarginRight: !0
        }, a.checked = !0, t.noCloneChecked = a.cloneNode(!0).checked, o.disabled = !0, t.optDisabled = !u.disabled;
        try {
          delete m.test
        } catch (y) {
          t.deleteExpando = !1
        }!m.addEventListener && m.attachEvent && m.fireEvent && (m.attachEvent("onclick", function() {
          t.noCloneEvent = !1
        }), m.cloneNode(!0).fireEvent("onclick")), a = n.createElement("input"), a.value = "t", a.setAttribute("type", "radio"), t.radioValue = a.value === "t", a.setAttribute("checked", "checked"), m.appendChild(a), l = n.createDocumentFragment(), l.appendChild(m.lastChild), t.checkClone = l.cloneNode(!0).cloneNode(!0).lastChild.checked, t.appendChecked = a.checked, l.removeChild(a), l.appendChild(m), m.innerHTML = "", e.getComputedStyle && (f = n.createElement("div"), f.style.width = "0", f.style.marginRight = "0", m.style.width = "2px", m.appendChild(f), t.reliableMarginRight = (parseInt((e.getComputedStyle(f, null) || {
          marginRight: 0
        }).marginRight, 10) || 0) === 0);
        if (m.attachEvent) for (d in {
          submit: 1,
          change: 1,
          focusin: 1
        }) p = "on" + d, v = p in m, v || (m.setAttribute(p, "return;"), v = typeof m[p] == "function"), t[d + "Bubbles"] = v;
        return l.removeChild(m), l = o = u = f = m = a = null, s(function() {
          var e, r, i, o, u, a, f, l, h, p, d, g = n.getElementsByTagName("body")[0];
          if (!g) return;
          f = 1, l = "position:absolute;top:0;left:0;width:1px;height:1px;margin:0;", h = "visibility:hidden;border:0;", p = "style='" + l + "border:5px solid #000;padding:0;'", d = "<div " + p + "><div></div></div>" + "<table " + p + " cellpadding='0' cellspacing='0'>" + "<tr><td></td></tr></table>", e = n.createElement("div"), e.style.cssText = h + "width:0;height:0;position:static;top:0;margin-top:" + f + "px", g.insertBefore(e, g.firstChild), m = n.createElement("div"), e.appendChild(m), m.innerHTML = "<table><tr><td style='padding:0;border:0;display:none'></td><td>t</td></tr></table>", c = m.getElementsByTagName("td"), v = c[0].offsetHeight === 0, c[0].style.display = "", c[1].style.display = "none", t.reliableHiddenOffsets = v && c[0].offsetHeight === 0, m.innerHTML = "", m.style.width = m.style.paddingLeft = "1px", s.boxModel = t.boxModel = m.offsetWidth === 2, typeof m.style.zoom != "undefined" && (m.style.display = "inline", m.style.zoom = 1, t.inlineBlockNeedsLayout = m.offsetWidth === 2, m.style.display = "", m.innerHTML = "<div style='width:4px;'></div>", t.shrinkWrapBlocks = m.offsetWidth !== 2), m.style.cssText = l + h, m.innerHTML = d, r = m.firstChild, i = r.firstChild, u = r.nextSibling.firstChild.firstChild, a = {
            doesNotAddBorder: i.offsetTop !== 5,
            doesAddBorderForTableAndCells: u.offsetTop === 5
          }, i.style.position = "fixed", i.style.top = "20px", a.fixedPosition = i.offsetTop === 20 || i.offsetTop === 15, i.style.position = i.style.top = "", r.style.overflow = "hidden", r.style.position = "relative", a.subtractsBorderForOverflowNotVisible = i.offsetTop === -5, a.doesNotIncludeMarginInBodyOffset = g.offsetTop !== f, g.removeChild(e), m = e = null, s.extend(t, a)
        }), t
      }();
      var f = /^(?:\{.*\}|\[.*\])$/,
          l = /([A-Z])/g;
      s.extend({
        cache: {},
        uuid: 0,
        expando: "jQuery" + (s.fn.jquery + Math.random()).replace(/\D/g, ""),
        noData: {
          embed: !0,
          object: "clsid:D27CDB6E-AE6D-11cf-96B8-444553540000",
          applet: !0
        },
        hasData: function(e) {
          return e = e.nodeType ? s.cache[e[s.expando]] : e[s.expando], !! e && !h(e)
        },
        data: function(e, n, r, i) {
          if (!s.acceptData(e)) return;
          var o, u, a, f = s.expando,
              l = typeof n == "string",
              c = e.nodeType,
              h = c ? s.cache : e,
              p = c ? e[f] : e[f] && f,
              d = n === "events";
          if ((!p || !h[p] || !d && !i && !h[p].data) && l && r === t) return;
          p || (c ? e[f] = p = ++s.uuid : p = f), h[p] || (h[p] = {}, c || (h[p].toJSON = s.noop));
          if (typeof n == "object" || typeof n == "function") i ? h[p] = s.extend(h[p], n) : h[p].data = s.extend(h[p].data, n);
          return o = u = h[p], i || (u.data || (u.data = {}), u = u.data), r !== t && (u[s.camelCase(n)] = r), d && !u[n] ? o.events : (l ? (a = u[n], a == null && (a = u[s.camelCase(n)])) : a = u, a)
        },
        removeData: function(e, t, n) {
          if (!s.acceptData(e)) return;
          var r, i, o, u = s.expando,
              a = e.nodeType,
              f = a ? s.cache : e,
              l = a ? e[u] : u;
          if (!f[l]) return;
          if (t) {
            r = n ? f[l] : f[l].data;
            if (r) {
              s.isArray(t) || (t in r ? t = [t] : (t = s.camelCase(t), t in r ? t = [t] : t = t.split(" ")));
              for (i = 0, o = t.length; i < o; i++) delete r[t[i]];
              if (!(n ? h : s.isEmptyObject)(r)) return
            }
          }
          if (!n) {
            delete f[l].data;
            if (!h(f[l])) return
          }
          s.support.deleteExpando || !f.setInterval ? delete f[l] : f[l] = null, a && (s.support.deleteExpando ? delete e[u] : e.removeAttribute ? e.removeAttribute(u) : e[u] = null)
        },
        _data: function(e, t, n) {
          return s.data(e, t, n, !0)
        },
        acceptData: function(e) {
          if (e.nodeName) {
            var t = s.noData[e.nodeName.toLowerCase()];
            if (t) return t !== !0 && e.getAttribute("classid") === t
          }
          return !0
        }
      }), s.fn.extend({
        data: function(e, n) {
          var r, i, o, u = null;
          if (typeof e == "undefined") {
            if (this.length) {
              u = s.data(this[0]);
              if (this[0].nodeType === 1 && !s._data(this[0], "parsedAttrs")) {
                i = this[0].attributes;
                for (var a = 0, f = i.length; a < f; a++) o = i[a].name, o.indexOf("data-") === 0 && (o = s.camelCase(o.substring(5)), c(this[0], o, u[o]));
                s._data(this[0], "parsedAttrs", !0)
              }
            }
            return u
          }
          return typeof e == "object" ? this.each(function() {
            s.data(this, e)
          }) : (r = e.split("."), r[1] = r[1] ? "." + r[1] : "", n === t ? (u = this.triggerHandler("getData" + r[1] + "!", [r[0]]), u === t && this.length && (u = s.data(this[0], e), u = c(this[0], e, u)), u === t && r[1] ? this.data(r[0]) : u) : this.each(function() {
            var t = s(this),
                i = [r[0], n];
            t.triggerHandler("setData" + r[1] + "!", i), s.data(this, e, n), t.triggerHandler("changeData" + r[1] + "!", i)
          }))
        },
        removeData: function(e) {
          return this.each(function() {
            s.removeData(this, e)
          })
        }
      }), s.extend({
        _mark: function(e, t) {
          e && (t = (t || "fx") + "mark", s._data(e, t, (s._data(e, t) || 0) + 1))
        },
        _unmark: function(e, t, n) {
          e !== !0 && (n = t, t = e, e = !1);
          if (t) {
            n = n || "fx";
            var r = n + "mark",
                i = e ? 0 : (s._data(t, r) || 1) - 1;
            i ? s._data(t, r, i) : (s.removeData(t, r, !0), p(t, n, "mark"))
          }
        },
        queue: function(e, t, n) {
          var r;
          if (e) return t = (t || "fx") + "queue", r = s._data(e, t), n && (!r || s.isArray(n) ? r = s._data(e, t, s.makeArray(n)) : r.push(n)), r || []
        },
        dequeue: function(e, t) {
          t = t || "fx";
          var n = s.queue(e, t),
              r = n.shift(),
              i = {};
          r === "inprogress" && (r = n.shift()), r && (t === "fx" && n.unshift("inprogress"), s._data(e, t + ".run", i), r.call(e, function() {
            s.dequeue(e, t)
          }, i)), n.length || (s.removeData(e, t + "queue " + t + ".run", !0), p(e, t, "queue"))
        }
      }), s.fn.extend({
        queue: function(e, n) {
          return typeof e != "string" && (n = e, e = "fx"), n === t ? s.queue(this[0], e) : this.each(function() {
            var t = s.queue(this, e, n);
            e === "fx" && t[0] !== "inprogress" && s.dequeue(this, e)
          })
        },
        dequeue: function(e) {
          return this.each(function() {
            s.dequeue(this, e)
          })
        },
        delay: function(e, t) {
          return e = s.fx ? s.fx.speeds[e] || e : e, t = t || "fx", this.queue(t, function(t, n) {
            var r = setTimeout(t, e);
            n.stop = function() {
              clearTimeout(r)
            }
          })
        },
        clearQueue: function(e) {
          return this.queue(e || "fx", [])
        },
        promise: function(e, n) {
          function h() {
            --u || r.resolveWith(i, [i])
          }
          typeof e != "string" && (n = e, e = t), e = e || "fx";
          var r = s.Deferred(),
              i = this,
              o = i.length,
              u = 1,
              a = e + "defer",
              f = e + "queue",
              l = e + "mark",
              c;
          while (o--) if (c = s.data(i[o], a, t, !0) || (s.data(i[o], f, t, !0) || s.data(i[o], l, t, !0)) && s.data(i[o], a, s.Callbacks("once memory"), !0)) u++, c.add(h);
          return h(), r.promise()
        }
      });
      var d = /[\n\t\r]/g,
          v = /\s+/,
          m = /\r/g,
          g = /^(?:button|input)$/i,
          y = /^(?:button|input|object|select|textarea)$/i,
          b = /^a(?:rea)?$/i,
          w = /^(?:autofocus|autoplay|async|checked|controls|defer|disabled|hidden|loop|multiple|open|readonly|required|scoped|selected)$/i,
          E = s.support.getSetAttribute,
          S, x, T;
      s.fn.extend({
        attr: function(e, t) {
          return s.access(this, e, t, !0, s.attr)
        },
        removeAttr: function(e) {
          return this.each(function() {
            s.removeAttr(this, e)
          })
        },
        prop: function(e, t) {
          return s.access(this, e, t, !0, s.prop)
        },
        removeProp: function(e) {
          return e = s.propFix[e] || e, this.each(function() {
            try {
              this[e] = t, delete this[e]
            } catch (n) {}
          })
        },
        addClass: function(e) {
          var t, n, r, i, o, u, a;
          if (s.isFunction(e)) return this.each(function(t) {
            s(this).addClass(e.call(this, t, this.className))
          });
          if (e && typeof e == "string") {
            t = e.split(v);
            for (n = 0, r = this.length; n < r; n++) {
              i = this[n];
              if (i.nodeType === 1) if (!i.className && t.length === 1) i.className = e;
              else {
                o = " " + i.className + " ";
                for (u = 0, a = t.length; u < a; u++)~o.indexOf(" " + t[u] + " ") || (o += t[u] + " ");
                i.className = s.trim(o)
              }
            }
          }
          return this
        },
        removeClass: function(e) {
          var n, r, i, o, u, a, f;
          if (s.isFunction(e)) return this.each(function(t) {
            s(this).removeClass(e.call(this, t, this.className))
          });
          if (e && typeof e == "string" || e === t) {
            n = (e || "").split(v);
            for (r = 0, i = this.length; r < i; r++) {
              o = this[r];
              if (o.nodeType === 1 && o.className) if (e) {
                u = (" " + o.className + " ").replace(d, " ");
                for (a = 0, f = n.length; a < f; a++) u = u.replace(" " + n[a] + " ", " ");
                o.className = s.trim(u)
              } else o.className = ""
            }
          }
          return this
        },
        toggleClass: function(e, t) {
          var n = typeof e,
              r = typeof t == "boolean";
          return s.isFunction(e) ? this.each(function(n) {
            s(this).toggleClass(e.call(this, n, this.className, t), t)
          }) : this.each(function() {
            if (n === "string") {
              var i, o = 0,
                  u = s(this),
                  a = t,
                  f = e.split(v);
              while (i = f[o++]) a = r ? a : !u.hasClass(i), u[a ? "addClass" : "removeClass"](i)
            } else if (n === "undefined" || n === "boolean") this.className && s._data(this, "__className__", this.className), this.className = this.className || e === !1 ? "" : s._data(this, "__className__") || ""
          })
        },
        hasClass: function(e) {
          var t = " " + e + " ",
              n = 0,
              r = this.length;
          for (; n < r; n++) if (this[n].nodeType === 1 && (" " + this[n].className + " ").replace(d, " ").indexOf(t) > -1) return !0;
          return !1
        },
        val: function(e) {
          var n, r, i, o = this[0];
          if (!arguments.length) {
            if (o) return n = s.valHooks[o.nodeName.toLowerCase()] || s.valHooks[o.type], n && "get" in n && (r = n.get(o, "value")) !== t ? r : (r = o.value, typeof r == "string" ? r.replace(m, "") : r == null ? "" : r);
            return
          }
          return i = s.isFunction(e), this.each(function(r) {
            var o = s(this),
                u;
            if (this.nodeType !== 1) return;
            i ? u = e.call(this, r, o.val()) : u = e, u == null ? u = "" : typeof u == "number" ? u += "" : s.isArray(u) && (u = s.map(u, function(e) {
              return e == null ? "" : e + ""
            })), n = s.valHooks[this.nodeName.toLowerCase()] || s.valHooks[this.type];
            if (!n || !("set" in n) || n.set(this, u, "value") === t) this.value = u
          })
        }
      }), s.extend({
        valHooks: {
          option: {
            get: function(e) {
              var t = e.attributes.value;
              return !t || t.specified ? e.value : e.text
            }
          },
          select: {
            get: function(e) {
              var t, n, r, i, o = e.selectedIndex,
                  u = [],
                  a = e.options,
                  f = e.type === "select-one";
              if (o < 0) return null;
              n = f ? o : 0, r = f ? o + 1 : a.length;
              for (; n < r; n++) {
                i = a[n];
                if (i.selected && (s.support.optDisabled ? !i.disabled : i.getAttribute("disabled") === null) && (!i.parentNode.disabled || !s.nodeName(i.parentNode, "optgroup"))) {
                  t = s(i).val();
                  if (f) return t;
                  u.push(t)
                }
              }
              return f && !u.length && a.length ? s(a[o]).val() : u
            },
            set: function(e, t) {
              var n = s.makeArray(t);
              return s(e).find("option").each(function() {
                this.selected = s.inArray(s(this).val(), n) >= 0
              }), n.length || (e.selectedIndex = -1), n
            }
          }
        },
        attrFn: {
          val: !0,
          css: !0,
          html: !0,
          text: !0,
          data: !0,
          width: !0,
          height: !0,
          offset: !0
        },
        attr: function(e, n, r, i) {
          var o, u, a, f = e.nodeType;
          if (!e || f === 3 || f === 8 || f === 2) return;
          if (i && n in s.attrFn) return s(e)[n](r);
          if (typeof e.getAttribute == "undefined") return s.prop(e, n, r);
          a = f !== 1 || !s.isXMLDoc(e), a && (n = n.toLowerCase(), u = s.attrHooks[n] || (w.test(n) ? x : S));
          if (r !== t) {
            if (r === null) {
              s.removeAttr(e, n);
              return
            }
            return u && "set" in u && a && (o = u.set(e, r, n)) !== t ? o : (e.setAttribute(n, "" + r), r)
          }
          return u && "get" in u && a && (o = u.get(e, n)) !== null ? o : (o = e.getAttribute(n), o === null ? t : o)
        },
        removeAttr: function(e, t) {
          var n, r, i, o, u = 0;
          if (t && e.nodeType === 1) {
            r = t.toLowerCase().split(v), o = r.length;
            for (; u < o; u++) i = r[u], i && (n = s.propFix[i] || i, s.attr(e, i, ""), e.removeAttribute(E ? i : n), w.test(i) && n in e && (e[n] = !1))
          }
        },
        attrHooks: {
          type: {
            set: function(e, t) {
              if (g.test(e.nodeName) && e.parentNode) s.error("type property can't be changed");
              else if (!s.support.radioValue && t === "radio" && s.nodeName(e, "input")) {
                var n = e.value;
                return e.setAttribute("type", t), n && (e.value = n), t
              }
            }
          },
          value: {
            get: function(e, t) {
              return S && s.nodeName(e, "button") ? S.get(e, t) : t in e ? e.value : null
            },
            set: function(e, t, n) {
              if (S && s.nodeName(e, "button")) return S.set(e, t, n);
              e.value = t
            }
          }
        },
        propFix: {
          tabindex: "tabIndex",
          readonly: "readOnly",
          "for": "htmlFor",
          "class": "className",
          maxlength: "maxLength",
          cellspacing: "cellSpacing",
          cellpadding: "cellPadding",
          rowspan: "rowSpan",
          colspan: "colSpan",
          usemap: "useMap",
          frameborder: "frameBorder",
          contenteditable: "contentEditable"
        },
        prop: function(e, n, r) {
          var i, o, u, a = e.nodeType;
          if (!e || a === 3 || a === 8 || a === 2) return;
          return u = a !== 1 || !s.isXMLDoc(e), u && (n = s.propFix[n] || n, o = s.propHooks[n]), r !== t ? o && "set" in o && (i = o.set(e, r, n)) !== t ? i : e[n] = r : o && "get" in o && (i = o.get(e, n)) !== null ? i : e[n]
        },
        propHooks: {
          tabIndex: {
            get: function(e) {
              var n = e.getAttributeNode("tabindex");
              return n && n.specified ? parseInt(n.value, 10) : y.test(e.nodeName) || b.test(e.nodeName) && e.href ? 0 : t
            }
          }
        }
      }), s.attrHooks.tabindex = s.propHooks.tabIndex, x = {
        get: function(e, n) {
          var r, i = s.prop(e, n);
          return i === !0 || typeof i != "boolean" && (r = e.getAttributeNode(n)) && r.nodeValue !== !1 ? n.toLowerCase() : t
        },
        set: function(e, t, n) {
          var r;
          return t === !1 ? s.removeAttr(e, n) : (r = s.propFix[n] || n, r in e && (e[r] = !0), e.setAttribute(n, n.toLowerCase())), n
        }
      }, E || (T = {
        name: !0,
        id: !0
      }, S = s.valHooks.button = {
        get: function(e, n) {
          var r;
          return r = e.getAttributeNode(n), r && (T[n] ? r.nodeValue !== "" : r.specified) ? r.nodeValue : t
        },
        set: function(e, t, r) {
          var i = e.getAttributeNode(r);
          return i || (i = n.createAttribute(r), e.setAttributeNode(i)), i.nodeValue = t + ""
        }
      }, s.attrHooks.tabindex.set = S.set, s.each(["width", "height"], function(e, t) {
        s.attrHooks[t] = s.extend(s.attrHooks[t], {
          set: function(e, n) {
            if (n === "") return e.setAttribute(t, "auto"), n
          }
        })
      }), s.attrHooks.contenteditable = {
        get: S.get,
        set: function(e, t, n) {
          t === "" && (t = "false"), S.set(e, t, n)
        }
      }), s.support.hrefNormalized || s.each(["href", "src", "width", "height"], function(e, n) {
        s.attrHooks[n] = s.extend(s.attrHooks[n], {
          get: function(e) {
            var r = e.getAttribute(n, 2);
            return r === null ? t : r
          }
        })
      }), s.support.style || (s.attrHooks.style = {
        get: function(e) {
          return e.style.cssText.toLowerCase() || t
        },
        set: function(e, t) {
          return e.style.cssText = "" + t
        }
      }), s.support.optSelected || (s.propHooks.selected = s.extend(s.propHooks.selected, {
        get: function(e) {
          var t = e.parentNode;
          return t && (t.selectedIndex, t.parentNode && t.parentNode.selectedIndex), null
        }
      })), s.support.enctype || (s.propFix.enctype = "encoding"), s.support.checkOn || s.each(["radio", "checkbox"], function() {
        s.valHooks[this] = {
          get: function(e) {
            return e.getAttribute("value") === null ? "on" : e.value
          }
        }
      }), s.each(["radio", "checkbox"], function() {
        s.valHooks[this] = s.extend(s.valHooks[this], {
          set: function(e, t) {
            if (s.isArray(t)) return e.checked = s.inArray(s(e).val(), t) >= 0
          }
        })
      });
      var N = /^(?:textarea|input|select)$/i,
          C = /^([^\.]*)?(?:\.(.+))?$/,
          k = /\bhover(\.\S+)?\b/,
          L = /^key/,
          A = /^(?:mouse|contextmenu)|click/,
          O = /^(?:focusinfocus|focusoutblur)$/,
          M = /^(\w*)(?:#([\w\-]+))?(?:\.([\w\-]+))?$/,
          _ = function(e) {
          var t = M.exec(e);
          return t && (t[1] = (t[1] || "").toLowerCase(), t[3] = t[3] && new RegExp("(?:^|\\s)" + t[3] + "(?:\\s|$)")), t
          },
          D = function(e, t) {
          var n = e.attributes || {};
          return (!t[1] || e.nodeName.toLowerCase() === t[1]) && (!t[2] || (n.id || {}).value === t[2]) && (!t[3] || t[3].test((n["class"] || {}).value))
          },
          P = function(e) {
          return s.event.special.hover ? e : e.replace(k, "mouseenter$1 mouseleave$1")
          };
      s.event = {
        add: function(e, n, r, i, o) {
          var u, a, f, l, c, h, p, d, v, m, g, y;
          if (e.nodeType === 3 || e.nodeType === 8 || !n || !r || !(u = s._data(e))) return;
          r.handler && (v = r, r = v.handler), r.guid || (r.guid = s.guid++), f = u.events, f || (u.events = f = {}), a = u.handle, a || (u.handle = a = function(e) {
            return typeof s == "undefined" || !! e && s.event.triggered === e.type ? t : s.event.dispatch.apply(a.elem, arguments)
          }, a.elem = e), n = s.trim(P(n)).split(" ");
          for (l = 0; l < n.length; l++) {
            c = C.exec(n[l]) || [], h = c[1], p = (c[2] || "").split(".").sort(), y = s.event.special[h] || {}, h = (o ? y.delegateType : y.bindType) || h, y = s.event.special[h] || {}, d = s.extend({
              type: h,
              origType: c[1],
              data: i,
              handler: r,
              guid: r.guid,
              selector: o,
              quick: _(o),
              namespace: p.join(".")
            }, v), g = f[h];
            if (!g) {
              g = f[h] = [], g.delegateCount = 0;
              if (!y.setup || y.setup.call(e, i, p, a) === !1) e.addEventListener ? e.addEventListener(h, a, !1) : e.attachEvent && e.attachEvent("on" + h, a)
            }
            y.add && (y.add.call(e, d), d.handler.guid || (d.handler.guid = r.guid)), o ? g.splice(g.delegateCount++, 0, d) : g.push(d), s.event.global[h] = !0
          }
          e = null
        },
        global: {},
        remove: function(e, t, n, r, i) {
          var o = s.hasData(e) && s._data(e),
              u, a, f, l, c, h, p, d, v, m, g, y;
          if (!o || !(d = o.events)) return;
          t = s.trim(P(t || "")).split(" ");
          for (u = 0; u < t.length; u++) {
            a = C.exec(t[u]) || [], f = l = a[1], c = a[2];
            if (!f) {
              for (f in d) s.event.remove(e, f + t[u], n, r, !0);
              continue
            }
            v = s.event.special[f] || {}, f = (r ? v.delegateType : v.bindType) || f, g = d[f] || [], h = g.length, c = c ? new RegExp("(^|\\.)" + c.split(".").sort().join("\\.(?:.*\\.)?") + "(\\.|$)") : null;
            for (p = 0; p < g.length; p++) y = g[p], (i || l === y.origType) && (!n || n.guid === y.guid) && (!c || c.test(y.namespace)) && (!r || r === y.selector || r === "**" && y.selector) && (g.splice(p--, 1), y.selector && g.delegateCount--, v.remove && v.remove.call(e, y));
            g.length === 0 && h !== g.length && ((!v.teardown || v.teardown.call(e, c) === !1) && s.removeEvent(e, f, o.handle), delete d[f])
          }
          s.isEmptyObject(d) && (m = o.handle, m && (m.elem = null), s.removeData(e, ["events", "handle"], !0))
        },
        customEvent: {
          getData: !0,
          setData: !0,
          changeData: !0
        },
        trigger: function(n, r, i, o) {
          if (!i || i.nodeType !== 3 && i.nodeType !== 8) {
            var u = n.type || n,
                a = [],
                f, l, c, h, p, d, v, m, g, y;
            if (O.test(u + s.event.triggered)) return;
            u.indexOf("!") >= 0 && (u = u.slice(0, -1), l = !0), u.indexOf(".") >= 0 && (a = u.split("."), u = a.shift(), a.sort());
            if ((!i || s.event.customEvent[u]) && !s.event.global[u]) return;
            n = typeof n == "object" ? n[s.expando] ? n : new s.Event(u, n) : new s.Event(u), n.type = u, n.isTrigger = !0, n.exclusive = l, n.namespace = a.join("."), n.namespace_re = n.namespace ? new RegExp("(^|\\.)" + a.join("\\.(?:.*\\.)?") + "(\\.|$)") : null, d = u.indexOf(":") < 0 ? "on" + u : "";
            if (!i) {
              f = s.cache;
              for (c in f) f[c].events && f[c].events[u] && s.event.trigger(n, r, f[c].handle.elem, !0);
              return
            }
            n.result = t, n.target || (n.target = i), r = r != null ? s.makeArray(r) : [], r.unshift(n), v = s.event.special[u] || {};
            if (v.trigger && v.trigger.apply(i, r) === !1) return;
            g = [
              [i, v.bindType || u]
            ];
            if (!o && !v.noBubble && !s.isWindow(i)) {
              y = v.delegateType || u, h = O.test(y + u) ? i : i.parentNode, p = null;
              for (; h; h = h.parentNode) g.push([h, y]), p = h;
              p && p === i.ownerDocument && g.push([p.defaultView || p.parentWindow || e, y])
            }
            for (c = 0; c < g.length && !n.isPropagationStopped(); c++) h = g[c][0], n.type = g[c][1], m = (s._data(h, "events") || {})[n.type] && s._data(h, "handle"), m && m.apply(h, r), m = d && h[d], m && s.acceptData(h) && m.apply(h, r) === !1 && n.preventDefault();
            return n.type = u, !o && !n.isDefaultPrevented() && (!v._default || v._default.apply(i.ownerDocument, r) === !1) && (u !== "click" || !s.nodeName(i, "a")) && s.acceptData(i) && d && i[u] && (u !== "focus" && u !== "blur" || n.target.offsetWidth !== 0) && !s.isWindow(i) && (p = i[d], p && (i[d] = null), s.event.triggered = u, i[u](), s.event.triggered = t, p && (i[d] = p)), n.result
          }
          return
        },
        dispatch: function(n) {
          n = s.event.fix(n || e.event);
          var r = (s._data(this, "events") || {})[n.type] || [],
              i = r.delegateCount,
              o = [].slice.call(arguments, 0),
              u = !n.exclusive && !n.namespace,
              a = [],
              f, l, c, h, p, d, v, m, g, y, b;
          o[0] = n, n.delegateTarget = this;
          if (i && !n.target.disabled && (!n.button || n.type !== "click")) {
            h = s(this), h.context = this.ownerDocument || this;
            for (c = n.target; c != this; c = c.parentNode || this) {
              d = {}, m = [], h[0] = c;
              for (f = 0; f < i; f++) g = r[f], y = g.selector, d[y] === t && (d[y] = g.quick ? D(c, g.quick) : h.is(y)), d[y] && m.push(g);
              m.length && a.push({
                elem: c,
                matches: m
              })
            }
          }
          r.length > i && a.push({
            elem: this,
            matches: r.slice(i)
          });
          for (f = 0; f < a.length && !n.isPropagationStopped(); f++) {
            v = a[f], n.currentTarget = v.elem;
            for (l = 0; l < v.matches.length && !n.isImmediatePropagationStopped(); l++) {
              g = v.matches[l];
              if (u || !n.namespace && !g.namespace || n.namespace_re && n.namespace_re.test(g.namespace)) n.data = g.data, n.handleObj = g, p = ((s.event.special[g.origType] || {}).handle || g.handler).apply(v.elem, o), p !== t && (n.result = p, p === !1 && (n.preventDefault(), n.stopPropagation()))
            }
          }
          return n.result
        },
        props: "attrChange attrName relatedNode srcElement altKey bubbles cancelable ctrlKey currentTarget eventPhase metaKey relatedTarget shiftKey target timeStamp view which".split(" "),
        fixHooks: {},
        keyHooks: {
          props: "char charCode key keyCode".split(" "),
          filter: function(e, t) {
            return e.which == null && (e.which = t.charCode != null ? t.charCode : t.keyCode), e
          }
        },
        mouseHooks: {
          props: "button buttons clientX clientY fromElement offsetX offsetY pageX pageY screenX screenY toElement".split(" "),
          filter: function(e, r) {
            var i, s, o, u = r.button,
                a = r.fromElement;
            return e.pageX == null && r.clientX != null && (i = e.target.ownerDocument || n, s = i.documentElement, o = i.body, e.pageX = r.clientX + (s && s.scrollLeft || o && o.scrollLeft || 0) - (s && s.clientLeft || o && o.clientLeft || 0), e.pageY = r.clientY + (s && s.scrollTop || o && o.scrollTop || 0) - (s && s.clientTop || o && o.clientTop || 0)), !e.relatedTarget && a && (e.relatedTarget = a === e.target ? r.toElement : a), !e.which && u !== t && (e.which = u & 1 ? 1 : u & 2 ? 3 : u & 4 ? 2 : 0), e
          }
        },
        fix: function(e) {
          if (e[s.expando]) return e;
          var r, i, o = e,
              u = s.event.fixHooks[e.type] || {},
              a = u.props ? this.props.concat(u.props) : this.props;
          e = s.Event(o);
          for (r = a.length; r;) i = a[--r], e[i] = o[i];
          return e.target || (e.target = o.srcElement || n), e.target.nodeType === 3 && (e.target = e.target.parentNode), e.metaKey === t && (e.metaKey = e.ctrlKey), u.filter ? u.filter(e, o) : e
        },
        special: {
          ready: {
            setup: s.bindReady
          },
          load: {
            noBubble: !0
          },
          focus: {
            delegateType: "focusin"
          },
          blur: {
            delegateType: "focusout"
          },
          beforeunload: {
            setup: function(e, t, n) {
              s.isWindow(this) && (this.onbeforeunload = n)
            },
            teardown: function(e, t) {
              this.onbeforeunload === t && (this.onbeforeunload = null)
            }
          }
        },
        simulate: function(e, t, n, r) {
          var i = s.extend(new s.Event, n, {
            type: e,
            isSimulated: !0,
            originalEvent: {}
          });
          r ? s.event.trigger(i, null, t) : s.event.dispatch.call(t, i), i.isDefaultPrevented() && n.preventDefault()
        }
      }, s.event.handle = s.event.dispatch, s.removeEvent = n.removeEventListener ?
      function(e, t, n) {
        e.removeEventListener && e.removeEventListener(t, n, !1)
      } : function(e, t, n) {
        e.detachEvent && e.detachEvent("on" + t, n)
      }, s.Event = function(e, t) {
        if (!(this instanceof s.Event)) return new s.Event(e, t);
        e && e.type ? (this.originalEvent = e, this.type = e.type, this.isDefaultPrevented = e.defaultPrevented || e.returnValue === !1 || e.getPreventDefault && e.getPreventDefault() ? B : H) : this.type = e, t && s.extend(this, t), this.timeStamp = e && e.timeStamp || s.now(), this[s.expando] = !0
      }, s.Event.prototype = {
        preventDefault: function() {
          this.isDefaultPrevented = B;
          var e = this.originalEvent;
          if (!e) return;
          e.preventDefault ? e.preventDefault() : e.returnValue = !1
        },
        stopPropagation: function() {
          this.isPropagationStopped = B;
          var e = this.originalEvent;
          if (!e) return;
          e.stopPropagation && e.stopPropagation(), e.cancelBubble = !0
        },
        stopImmediatePropagation: function() {
          this.isImmediatePropagationStopped = B, this.stopPropagation()
        },
        isDefaultPrevented: H,
        isPropagationStopped: H,
        isImmediatePropagationStopped: H
      }, s.each({
        mouseenter: "mouseover",
        mouseleave: "mouseout"
      }, function(e, t) {
        s.event.special[e] = {
          delegateType: t,
          bindType: t,
          handle: function(e) {
            var n = this,
                r = e.relatedTarget,
                i = e.handleObj,
                o = i.selector,
                u;
            if (!r || r !== n && !s.contains(n, r)) e.type = i.origType, u = i.handler.apply(this, arguments), e.type = t;
            return u
          }
        }
      }), s.support.submitBubbles || (s.event.special.submit = {
        setup: function() {
          if (s.nodeName(this, "form")) return !1;
          s.event.add(this, "click._submit keypress._submit", function(e) {
            var n = e.target,
                r = s.nodeName(n, "input") || s.nodeName(n, "button") ? n.form : t;
            r && !r._submit_attached && (s.event.add(r, "submit._submit", function(e) {
              this.parentNode && !e.isTrigger && s.event.simulate("submit", this.parentNode, e, !0)
            }), r._submit_attached = !0)
          })
        },
        teardown: function() {
          if (s.nodeName(this, "form")) return !1;
          s.event.remove(this, "._submit")
        }
      }), s.support.changeBubbles || (s.event.special.change = {
        setup: function() {
          if (N.test(this.nodeName)) {
            if (this.type === "checkbox" || this.type === "radio") s.event.add(this, "propertychange._change", function(e) {
              e.originalEvent.propertyName === "checked" && (this._just_changed = !0)
            }), s.event.add(this, "click._change", function(e) {
              this._just_changed && !e.isTrigger && (this._just_changed = !1, s.event.simulate("change", this, e, !0))
            });
            return !1
          }
          s.event.add(this, "beforeactivate._change", function(e) {
            var t = e.target;
            N.test(t.nodeName) && !t._change_attached && (s.event.add(t, "change._change", function(e) {
              this.parentNode && !e.isSimulated && !e.isTrigger && s.event.simulate("change", this.parentNode, e, !0)
            }), t._change_attached = !0)
          })
        },
        handle: function(e) {
          var t = e.target;
          if (this !== t || e.isSimulated || e.isTrigger || t.type !== "radio" && t.type !== "checkbox") return e.handleObj.handler.apply(this, arguments)
        },
        teardown: function() {
          return s.event.remove(this, "._change"), N.test(this.nodeName)
        }
      }), s.support.focusinBubbles || s.each({
        focus: "focusin",
        blur: "focusout"
      }, function(e, t) {
        var r = 0,
            i = function(e) {
            s.event.simulate(t, e.target, s.event.fix(e), !0)
            };
        s.event.special[t] = {
          setup: function() {
            r++ === 0 && n.addEventListener(e, i, !0)
          },
          teardown: function() {
            --r === 0 && n.removeEventListener(e, i, !0)
          }
        }
      }), s.fn.extend({
        on: function(e, n, r, i, o) {
          var u, a;
          if (typeof e == "object") {
            typeof n != "string" && (r = n, n = t);
            for (a in e) this.on(a, n, r, e[a], o);
            return this
          }
          r == null && i == null ? (i = n, r = n = t) : i == null && (typeof n == "string" ? (i = r, r = t) : (i = r, r = n, n = t));
          if (i === !1) i = H;
          else if (!i) return this;
          return o === 1 && (u = i, i = function(e) {
            return s().off(e), u.apply(this, arguments)
          }, i.guid = u.guid || (u.guid = s.guid++)), this.each(function() {
            s.event.add(this, e, i, r, n)
          })
        },
        one: function(e, t, n, r) {
          return this.on.call(this, e, t, n, r, 1)
        },
        off: function(e, n, r) {
          if (e && e.preventDefault && e.handleObj) {
            var i = e.handleObj;
            return s(e.delegateTarget).off(i.namespace ? i.type + "." + i.namespace : i.type, i.selector, i.handler), this
          }
          if (typeof e == "object") {
            for (var o in e) this.off(o, n, e[o]);
            return this
          }
          if (n === !1 || typeof n == "function") r = n, n = t;
          return r === !1 && (r = H), this.each(function() {
            s.event.remove(this, e, r, n)
          })
        },
        bind: function(e, t, n) {
          return this.on(e, null, t, n)
        },
        unbind: function(e, t) {
          return this.off(e, null, t)
        },
        live: function(e, t, n) {
          return s(this.context).on(e, this.selector, t, n), this
        },
        die: function(e, t) {
          return s(this.context).off(e, this.selector || "**", t), this
        },
        delegate: function(e, t, n, r) {
          return this.on(t, e, n, r)
        },
        undelegate: function(e, t, n) {
          return arguments.length == 1 ? this.off(e, "**") : this.off(t, e, n)
        },
        trigger: function(e, t) {
          return this.each(function() {
            s.event.trigger(e, t, this)
          })
        },
        triggerHandler: function(e, t) {
          if (this[0]) return s.event.trigger(e, t, this[0], !0)
        },
        toggle: function(e) {
          var t = arguments,
              n = e.guid || s.guid++,
              r = 0,
              i = function(n) {
              var i = (s._data(this, "lastToggle" + e.guid) || 0) % r;
              return s._data(this, "lastToggle" + e.guid, i + 1), n.preventDefault(), t[i].apply(this, arguments) || !1
              };
          i.guid = n;
          while (r < t.length) t[r++].guid = n;
          return this.click(i)
        },
        hover: function(e, t) {
          return this.mouseenter(e).mouseleave(t || e)
        }
      }), s.each("blur focus focusin focusout load resize scroll unload click dblclick mousedown mouseup mousemove mouseover mouseout mouseenter mouseleave change select submit keydown keypress keyup error contextmenu".split(" "), function(e, t) {
        s.fn[t] = function(e, n) {
          return n == null && (n = e, e = null), arguments.length > 0 ? this.on(t, null, e, n) : this.trigger(t)
        }, s.attrFn && (s.attrFn[t] = !0), L.test(t) && (s.event.fixHooks[t] = s.event.keyHooks), A.test(t) && (s.event.fixHooks[t] = s.event.mouseHooks)
      }), function() {
        function S(e, t, n, i, s, o) {
          for (var u = 0, a = i.length; u < a; u++) {
            var f = i[u];
            if (f) {
              var l = !1;
              f = f[e];
              while (f) {
                if (f[r] === n) {
                  l = i[f.sizset];
                  break
                }
                f.nodeType === 1 && !o && (f[r] = n, f.sizset = u);
                if (f.nodeName.toLowerCase() === t) {
                  l = f;
                  break
                }
                f = f[e]
              }
              i[u] = l
            }
          }
        }
        function x(e, t, n, i, s, o) {
          for (var u = 0, a = i.length; u < a; u++) {
            var f = i[u];
            if (f) {
              var l = !1;
              f = f[e];
              while (f) {
                if (f[r] === n) {
                  l = i[f.sizset];
                  break
                }
                if (f.nodeType === 1) {
                  o || (f[r] = n, f.sizset = u);
                  if (typeof t != "string") {
                    if (f === t) {
                      l = !0;
                      break
                    }
                  } else if (h.filter(t, [f]).length > 0) {
                    l = f;
                    break
                  }
                }
                f = f[e]
              }
              i[u] = l
            }
          }
        }
        var e = /((?:\((?:\([^()]+\)|[^()]+)+\)|\[(?:\[[^\[\]]*\]|['"][^'"]*['"]|[^\[\]'"]+)+\]|\\.|[^ >+~,(\[\\]+)+|[>+~])(\s*,\s*)?((?:.|\r|\n)*)/g,
            r = "sizcache" + (Math.random() + "").replace(".", ""),
            i = 0,
            o = Object.prototype.toString,
            u = !1,
            a = !0,
            f = /\\/g,
            l = /\r\n/g,
            c = /\W/;
        [0, 0].sort(function() {
          return a = !1, 0
        });
        var h = function(t, r, i, s) {
            i = i || [], r = r || n;
            var u = r;
            if (r.nodeType !== 1 && r.nodeType !== 9) return [];
            if (!t || typeof t != "string") return i;
            var a, f, l, c, p, m, g, b, w = !0,
                E = h.isXML(r),
                S = [],
                x = t;
            do {
              e.exec(""), a = e.exec(x);
              if (a) {
                x = a[3], S.push(a[1]);
                if (a[2]) {
                  c = a[3];
                  break
                }
              }
            } while (a);
            if (S.length > 1 && v.exec(t)) if (S.length === 2 && d.relative[S[0]]) f = T(S[0] + S[1], r, s);
            else {
              f = d.relative[S[0]] ? [r] : h(S.shift(), r);
              while (S.length) t = S.shift(), d.relative[t] && (t += S.shift()), f = T(t, f, s)
            } else {
              !s && S.length > 1 && r.nodeType === 9 && !E && d.match.ID.test(S[0]) && !d.match.ID.test(S[S.length - 1]) && (p = h.find(S.shift(), r, E), r = p.expr ? h.filter(p.expr, p.set)[0] : p.set[0]);
              if (r) {
                p = s ? {
                  expr: S.pop(),
                  set: y(s)
                } : h.find(S.pop(), S.length !== 1 || S[0] !== "~" && S[0] !== "+" || !r.parentNode ? r : r.parentNode, E), f = p.expr ? h.filter(p.expr, p.set) : p.set, S.length > 0 ? l = y(f) : w = !1;
                while (S.length) m = S.pop(), g = m, d.relative[m] ? g = S.pop() : m = "", g == null && (g = r), d.relative[m](l, g, E)
              } else l = S = []
            }
            l || (l = f), l || h.error(m || t);
            if (o.call(l) === "[object Array]") if (!w) i.push.apply(i, l);
            else if (r && r.nodeType === 1) for (b = 0; l[b] != null; b++) l[b] && (l[b] === !0 || l[b].nodeType === 1 && h.contains(r, l[b])) && i.push(f[b]);
            else for (b = 0; l[b] != null; b++) l[b] && l[b].nodeType === 1 && i.push(f[b]);
            else y(l, i);
            return c && (h(c, u, i, s), h.uniqueSort(i)), i
            };
        h.uniqueSort = function(e) {
          if (w) {
            u = a, e.sort(w);
            if (u) for (var t = 1; t < e.length; t++) e[t] === e[t - 1] && e.splice(t--, 1)
          }
          return e
        }, h.matches = function(e, t) {
          return h(e, null, null, t)
        }, h.matchesSelector = function(e, t) {
          return h(t, null, null, [e]).length > 0
        }, h.find = function(e, t, n) {
          var r, i, s, o, u, a;
          if (!e) return [];
          for (i = 0, s = d.order.length; i < s; i++) {
            u = d.order[i];
            if (o = d.leftMatch[u].exec(e)) {
              a = o[1], o.splice(1, 1);
              if (a.substr(a.length - 1) !== "\\") {
                o[1] = (o[1] || "").replace(f, ""), r = d.find[u](o, t, n);
                if (r != null) {
                  e = e.replace(d.match[u], "");
                  break
                }
              }
            }
          }
          return r || (r = typeof t.getElementsByTagName != "undefined" ? t.getElementsByTagName("*") : []), {
            set: r,
            expr: e
          }
        }, h.filter = function(e, n, r, i) {
          var s, o, u, a, f, l, c, p, v, m = e,
              g = [],
              y = n,
              b = n && n[0] && h.isXML(n[0]);
          while (e && n.length) {
            for (u in d.filter) if ((s = d.leftMatch[u].exec(e)) != null && s[2]) {
              l = d.filter[u], c = s[1], o = !1, s.splice(1, 1);
              if (c.substr(c.length - 1) === "\\") continue;
              y === g && (g = []);
              if (d.preFilter[u]) {
                s = d.preFilter[u](s, y, r, g, i, b);
                if (!s) o = a = !0;
                else if (s === !0) continue
              }
              if (s) for (p = 0;
              (f = y[p]) != null; p++) f && (a = l(f, s, p, y), v = i ^ a, r && a != null ? v ? o = !0 : y[p] = !1 : v && (g.push(f), o = !0));
              if (a !== t) {
                r || (y = g), e = e.replace(d.match[u], "");
                if (!o) return [];
                break
              }
            }
            if (e === m) {
              if (o != null) break;
              h.error(e)
            }
            m = e
          }
          return y
        }, h.error = function(e) {
          throw new Error("Syntax error, unrecognized expression: " + e)
        };
        var p = h.getText = function(e) {
            var t, n, r = e.nodeType,
                i = "";
            if (r) {
              if (r === 1 || r === 9) {
                if (typeof e.textContent == "string") return e.textContent;
                if (typeof e.innerText == "string") return e.innerText.replace(l, "");
                for (e = e.firstChild; e; e = e.nextSibling) i += p(e)
              } else if (r === 3 || r === 4) return e.nodeValue
            } else for (t = 0; n = e[t]; t++) n.nodeType !== 8 && (i += p(n));
            return i
            },
            d = h.selectors = {
            order: ["ID", "NAME", "TAG"],
            match: {
              ID: /#((?:[\w\u00c0-\uFFFF\-]|\\.)+)/,
              CLASS: /\.((?:[\w\u00c0-\uFFFF\-]|\\.)+)/,
              NAME: /\[name=['"]*((?:[\w\u00c0-\uFFFF\-]|\\.)+)['"]*\]/,
              ATTR: /\[\s*((?:[\w\u00c0-\uFFFF\-]|\\.)+)\s*(?:(\S?=)\s*(?:(['"])(.*?)\3|(#?(?:[\w\u00c0-\uFFFF\-]|\\.)*)|)|)\s*\]/,
              TAG: /^((?:[\w\u00c0-\uFFFF\*\-]|\\.)+)/,
              CHILD: /:(only|nth|last|first)-child(?:\(\s*(even|odd|(?:[+\-]?\d+|(?:[+\-]?\d*)?n\s*(?:[+\-]\s*\d+)?))\s*\))?/,
              POS: /:(nth|eq|gt|lt|first|last|even|odd)(?:\((\d*)\))?(?=[^\-]|$)/,
              PSEUDO: /:((?:[\w\u00c0-\uFFFF\-]|\\.)+)(?:\((['"]?)((?:\([^\)]+\)|[^\(\)]*)+)\2\))?/
            },
            leftMatch: {},
            attrMap: {
              "class": "className",
              "for": "htmlFor"
            },
            attrHandle: {
              href: function(e) {
                return e.getAttribute("href")
              },
              type: function(e) {
                return e.getAttribute("type")
              }
            },
            relative: {
              "+": function(e, t) {
                var n = typeof t == "string",
                    r = n && !c.test(t),
                    i = n && !r;
                r && (t = t.toLowerCase());
                for (var s = 0, o = e.length, u; s < o; s++) if (u = e[s]) {
                  while ((u = u.previousSibling) && u.nodeType !== 1);
                  e[s] = i || u && u.nodeName.toLowerCase() === t ? u || !1 : u === t
                }
                i && h.filter(t, e, !0)
              },
              ">": function(e, t) {
                var n, r = typeof t == "string",
                    i = 0,
                    s = e.length;
                if (r && !c.test(t)) {
                  t = t.toLowerCase();
                  for (; i < s; i++) {
                    n = e[i];
                    if (n) {
                      var o = n.parentNode;
                      e[i] = o.nodeName.toLowerCase() === t ? o : !1
                    }
                  }
                } else {
                  for (; i < s; i++) n = e[i], n && (e[i] = r ? n.parentNode : n.parentNode === t);
                  r && h.filter(t, e, !0)
                }
              },
              "": function(e, t, n) {
                var r, s = i++,
                    o = x;
                typeof t == "string" && !c.test(t) && (t = t.toLowerCase(), r = t, o = S), o("parentNode", t, s, e, r, n)
              },
              "~": function(e, t, n) {
                var r, s = i++,
                    o = x;
                typeof t == "string" && !c.test(t) && (t = t.toLowerCase(), r = t, o = S), o("previousSibling", t, s, e, r, n)
              }
            },
            find: {
              ID: function(e, t, n) {
                if (typeof t.getElementById != "undefined" && !n) {
                  var r = t.getElementById(e[1]);
                  return r && r.parentNode ? [r] : []
                }
              },
              NAME: function(e, t) {
                if (typeof t.getElementsByName != "undefined") {
                  var n = [],
                      r = t.getElementsByName(e[1]);
                  for (var i = 0, s = r.length; i < s; i++) r[i].getAttribute("name") === e[1] && n.push(r[i]);
                  return n.length === 0 ? null : n
                }
              },
              TAG: function(e, t) {
                if (typeof t.getElementsByTagName != "undefined") return t.getElementsByTagName(e[1])
              }
            },
            preFilter: {
              CLASS: function(e, t, n, r, i, s) {
                e = " " + e[1].replace(f, "") + " ";
                if (s) return e;
                for (var o = 0, u;
                (u = t[o]) != null; o++) u && (i ^ (u.className && (" " + u.className + " ").replace(/[\t\n\r]/g, " ").indexOf(e) >= 0) ? n || r.push(u) : n && (t[o] = !1));
                return !1
              },
              ID: function(e) {
                return e[1].replace(f, "")
              },
              TAG: function(e, t) {
                return e[1].replace(f, "").toLowerCase()
              },
              CHILD: function(e) {
                if (e[1] === "nth") {
                  e[2] || h.error(e[0]), e[2] = e[2].replace(/^\+|\s*/g, "");
                  var t = /(-?)(\d*)(?:n([+\-]?\d*))?/.exec(e[2] === "even" && "2n" || e[2] === "odd" && "2n+1" || !/\D/.test(e[2]) && "0n+" + e[2] || e[2]);
                  e[2] = t[1] + (t[2] || 1) - 0, e[3] = t[3] - 0
                } else e[2] && h.error(e[0]);
                return e[0] = i++, e
              },
              ATTR: function(e, t, n, r, i, s) {
                var o = e[1] = e[1].replace(f, "");
                return !s && d.attrMap[o] && (e[1] = d.attrMap[o]), e[4] = (e[4] || e[5] || "").replace(f, ""), e[2] === "~=" && (e[4] = " " + e[4] + " "), e
              },
              PSEUDO: function(t, n, r, i, s) {
                if (t[1] === "not") {
                  if (!((e.exec(t[3]) || "").length > 1 || /^\w/.test(t[3]))) {
                    var o = h.filter(t[3], n, r, !0 ^ s);
                    return r || i.push.apply(i, o), !1
                  }
                  t[3] = h(t[3], null, null, n)
                } else if (d.match.POS.test(t[0]) || d.match.CHILD.test(t[0])) return !0;
                return t
              },
              POS: function(e) {
                return e.unshift(!0), e
              }
            },
            filters: {
              enabled: function(e) {
                return e.disabled === !1 && e.type !== "hidden"
              },
              disabled: function(e) {
                return e.disabled === !0
              },
              checked: function(e) {
                return e.checked === !0
              },
              selected: function(e) {
                return e.parentNode && e.parentNode.selectedIndex, e.selected === !0
              },
              parent: function(e) {
                return !!e.firstChild
              },
              empty: function(e) {
                return !e.firstChild
              },
              has: function(e, t, n) {
                return !!h(n[3], e).length
              },
              header: function(e) {
                return /h\d/i.test(e.nodeName)
              },
              text: function(e) {
                var t = e.getAttribute("type"),
                    n = e.type;
                return e.nodeName.toLowerCase() === "input" && "text" === n && (t === n || t === null)
              },
              radio: function(e) {
                return e.nodeName.toLowerCase() === "input" && "radio" === e.type
              },
              checkbox: function(e) {
                return e.nodeName.toLowerCase() === "input" && "checkbox" === e.type
              },
              file: function(e) {
                return e.nodeName.toLowerCase() === "input" && "file" === e.type
              },
              password: function(e) {
                return e.nodeName.toLowerCase() === "input" && "password" === e.type
              },
              submit: function(e) {
                var t = e.nodeName.toLowerCase();
                return (t === "input" || t === "button") && "submit" === e.type
              },
              image: function(e) {
                return e.nodeName.toLowerCase() === "input" && "image" === e.type
              },
              reset: function(e) {
                var t = e.nodeName.toLowerCase();
                return (t === "input" || t === "button") && "reset" === e.type
              },
              button: function(e) {
                var t = e.nodeName.toLowerCase();
                return t === "input" && "button" === e.type || t === "button"
              },
              input: function(e) {
                return /input|select|textarea|button/i.test(e.nodeName)
              },
              focus: function(e) {
                return e === e.ownerDocument.activeElement
              }
            },
            setFilters: {
              first: function(e, t) {
                return t === 0
              },
              last: function(e, t, n, r) {
                return t === r.length - 1
              },
              even: function(e, t) {
                return t % 2 === 0
              },
              odd: function(e, t) {
                return t % 2 === 1
              },
              lt: function(e, t, n) {
                return t < n[3] - 0
              },
              gt: function(e, t, n) {
                return t > n[3] - 0
              },
              nth: function(e, t, n) {
                return n[3] - 0 === t
              },
              eq: function(e, t, n) {
                return n[3] - 0 === t
              }
            },
            filter: {
              PSEUDO: function(e, t, n, r) {
                var i = t[1],
                    s = d.filters[i];
                if (s) return s(e, n, t, r);
                if (i === "contains") return (e.textContent || e.innerText || p([e]) || "").indexOf(t[3]) >= 0;
                if (i === "not") {
                  var o = t[3];
                  for (var u = 0, a = o.length; u < a; u++) if (o[u] === e) return !1;
                  return !0
                }
                h.error(i)
              },
              CHILD: function(e, t) {
                var n, i, s, o, u, a, f, l = t[1],
                    c = e;
                switch (l) {
                case "only":
                case "first":
                  while (c = c.previousSibling) if (c.nodeType === 1) return !1;
                  if (l === "first") return !0;
                  c = e;
                case "last":
                  while (c = c.nextSibling) if (c.nodeType === 1) return !1;
                  return !0;
                case "nth":
                  n = t[2], i = t[3];
                  if (n === 1 && i === 0) return !0;
                  s = t[0], o = e.parentNode;
                  if (o && (o[r] !== s || !e.nodeIndex)) {
                    a = 0;
                    for (c = o.firstChild; c; c = c.nextSibling) c.nodeType === 1 && (c.nodeIndex = ++a);
                    o[r] = s
                  }
                  return f = e.nodeIndex - i, n === 0 ? f === 0 : f % n === 0 && f / n >= 0
                }
              },
              ID: function(e, t) {
                return e.nodeType === 1 && e.getAttribute("id") === t
              },
              TAG: function(e, t) {
                return t === "*" && e.nodeType === 1 || !! e.nodeName && e.nodeName.toLowerCase() === t
              },
              CLASS: function(e, t) {
                return (" " + (e.className || e.getAttribute("class")) + " ").indexOf(t) > -1
              },
              ATTR: function(e, t) {
                var n = t[1],
                    r = h.attr ? h.attr(e, n) : d.attrHandle[n] ? d.attrHandle[n](e) : e[n] != null ? e[n] : e.getAttribute(n),
                    i = r + "",
                    s = t[2],
                    o = t[4];
                return r == null ? s === "!=" : !s && h.attr ? r != null : s === "=" ? i === o : s === "*=" ? i.indexOf(o) >= 0 : s === "~=" ? (" " + i + " ").indexOf(o) >= 0 : o ? s === "!=" ? i !== o : s === "^=" ? i.indexOf(o) === 0 : s === "$=" ? i.substr(i.length - o.length) === o : s === "|=" ? i === o || i.substr(0, o.length + 1) === o + "-" : !1 : i && r !== !1
              },
              POS: function(e, t, n, r) {
                var i = t[2],
                    s = d.setFilters[i];
                if (s) return s(e, n, t, r)
              }
            }
            },
            v = d.match.POS,
            m = function(e, t) {
            return "\\" + (t - 0 + 1)
            };
        for (var g in d.match) d.match[g] = new RegExp(d.match[g].source + /(?![^\[]*\])(?![^\(]*\))/.source), d.leftMatch[g] = new RegExp(/(^(?:.|\r|\n)*?)/.source + d.match[g].source.replace(/\\(\d+)/g, m));
        var y = function(e, t) {
            return e = Array.prototype.slice.call(e, 0), t ? (t.push.apply(t, e), t) : e
            };
        try {
          Array.prototype.slice.call(n.documentElement.childNodes, 0)[0].nodeType
        } catch (b) {
          y = function(e, t) {
            var n = 0,
                r = t || [];
            if (o.call(e) === "[object Array]") Array.prototype.push.apply(r, e);
            else if (typeof e.length == "number") for (var i = e.length; n < i; n++) r.push(e[n]);
            else for (; e[n]; n++) r.push(e[n]);
            return r
          }
        }
        var w, E;
        n.documentElement.compareDocumentPosition ? w = function(e, t) {
          return e === t ? (u = !0, 0) : !e.compareDocumentPosition || !t.compareDocumentPosition ? e.compareDocumentPosition ? -1 : 1 : e.compareDocumentPosition(t) & 4 ? -1 : 1
        } : (w = function(e, t) {
          if (e === t) return u = !0, 0;
          if (e.sourceIndex && t.sourceIndex) return e.sourceIndex - t.sourceIndex;
          var n, r, i = [],
              s = [],
              o = e.parentNode,
              a = t.parentNode,
              f = o;
          if (o === a) return E(e, t);
          if (!o) return -1;
          if (!a) return 1;
          while (f) i.unshift(f), f = f.parentNode;
          f = a;
          while (f) s.unshift(f), f = f.parentNode;
          n = i.length, r = s.length;
          for (var l = 0; l < n && l < r; l++) if (i[l] !== s[l]) return E(i[l], s[l]);
          return l === n ? E(e, s[l], -1) : E(i[l], t, 1)
        }, E = function(e, t, n) {
          if (e === t) return n;
          var r = e.nextSibling;
          while (r) {
            if (r === t) return -1;
            r = r.nextSibling
          }
          return 1
        }), function() {
          var e = n.createElement("div"),
              r = "script" + (new Date).getTime(),
              i = n.documentElement;
          e.innerHTML = "<a name='" + r + "'/>", i.insertBefore(e, i.firstChild), n.getElementById(r) && (d.find.ID = function(e, n, r) {
            if (typeof n.getElementById != "undefined" && !r) {
              var i = n.getElementById(e[1]);
              return i ? i.id === e[1] || typeof i.getAttributeNode != "undefined" && i.getAttributeNode("id").nodeValue === e[1] ? [i] : t : []
            }
          }, d.filter.ID = function(e, t) {
            var n = typeof e.getAttributeNode != "undefined" && e.getAttributeNode("id");
            return e.nodeType === 1 && n && n.nodeValue === t
          }), i.removeChild(e), i = e = null
        }(), function() {
          var e = n.createElement("div");
          e.appendChild(n.createComment("")), e.getElementsByTagName("*").length > 0 && (d.find.TAG = function(e, t) {
            var n = t.getElementsByTagName(e[1]);
            if (e[1] === "*") {
              var r = [];
              for (var i = 0; n[i]; i++) n[i].nodeType === 1 && r.push(n[i]);
              n = r
            }
            return n
          }), e.innerHTML = "<a href='#'></a>", e.firstChild && typeof e.firstChild.getAttribute != "undefined" && e.firstChild.getAttribute("href") !== "#" && (d.attrHandle.href = function(e) {
            return e.getAttribute("href", 2)
          }), e = null
        }(), n.querySelectorAll &&
        function() {
          var e = h,
              t = n.createElement("div"),
              r = "__sizzle__";
          t.innerHTML = "<p class='TEST'></p>";
          if (t.querySelectorAll && t.querySelectorAll(".TEST").length === 0) return;
          h = function(t, i, s, o) {
            i = i || n;
            if (!o && !h.isXML(i)) {
              var u = /^(\w+$)|^\.([\w\-]+$)|^#([\w\-]+$)/.exec(t);
              if (u && (i.nodeType === 1 || i.nodeType === 9)) {
                if (u[1]) return y(i.getElementsByTagName(t), s);
                if (u[2] && d.find.CLASS && i.getElementsByClassName) return y(i.getElementsByClassName(u[2]), s)
              }
              if (i.nodeType === 9) {
                if (t === "body" && i.body) return y([i.body], s);
                if (u && u[3]) {
                  var a = i.getElementById(u[3]);
                  if (!a || !a.parentNode) return y([], s);
                  if (a.id === u[3]) return y([a], s)
                }
                try {
                  return y(i.querySelectorAll(t), s)
                } catch (f) {}
              } else if (i.nodeType === 1 && i.nodeName.toLowerCase() !== "object") {
                var l = i,
                    c = i.getAttribute("id"),
                    p = c || r,
                    v = i.parentNode,
                    m = /^\s*[+~]/.test(t);
                c ? p = p.replace(/'/g, "\\$&") : i.setAttribute("id", p), m && v && (i = i.parentNode);
                try {
                  if (!m || v) return y(i.querySelectorAll("[id='" + p + "'] " + t), s)
                } catch (g) {} finally {
                  c || l.removeAttribute("id")
                }
              }
            }
            return e(t, i, s, o)
          };
          for (var i in e) h[i] = e[i];
          t = null
        }(), function() {
          var e = n.documentElement,
              t = e.matchesSelector || e.mozMatchesSelector || e.webkitMatchesSelector || e.msMatchesSelector;
          if (t) {
            var r = !t.call(n.createElement("div"), "div"),
                i = !1;
            try {
              t.call(n.documentElement, "[test!='']:sizzle")
            } catch (s) {
              i = !0
            }
            h.matchesSelector = function(e, n) {
              n = n.replace(/\=\s*([^'"\]]*)\s*\]/g, "='$1']");
              if (!h.isXML(e)) try {
                if (i || !d.match.PSEUDO.test(n) && !/!=/.test(n)) {
                  var s = t.call(e, n);
                  if (s || !r || e.document && e.document.nodeType !== 11) return s
                }
              } catch (o) {}
              return h(n, null, null, [e]).length > 0
            }
          }
        }(), function() {
          var e = n.createElement("div");
          e.innerHTML = "<div class='test e'></div><div class='test'></div>";
          if (!e.getElementsByClassName || e.getElementsByClassName("e").length === 0) return;
          e.lastChild.className = "e";
          if (e.getElementsByClassName("e").length === 1) return;
          d.order.splice(1, 0, "CLASS"), d.find.CLASS = function(e, t, n) {
            if (typeof t.getElementsByClassName != "undefined" && !n) return t.getElementsByClassName(e[1])
          }, e = null
        }(), n.documentElement.contains ? h.contains = function(e, t) {
          return e !== t && (e.contains ? e.contains(t) : !0)
        } : n.documentElement.compareDocumentPosition ? h.contains = function(e, t) {
          return !!(e.compareDocumentPosition(t) & 16)
        } : h.contains = function() {
          return !1
        }, h.isXML = function(e) {
          var t = (e ? e.ownerDocument || e : 0).documentElement;
          return t ? t.nodeName !== "HTML" : !1
        };
        var T = function(e, t, n) {
            var r, i = [],
                s = "",
                o = t.nodeType ? [t] : t;
            while (r = d.match.PSEUDO.exec(e)) s += r[0], e = e.replace(d.match.PSEUDO, "");
            e = d.relative[e] ? e + "*" : e;
            for (var u = 0, a = o.length; u < a; u++) h(e, o[u], i, n);
            return h.filter(s, i)
            };
        h.attr = s.attr, h.selectors.attrMap = {}, s.find = h, s.expr = h.selectors, s.expr[":"] = s.expr.filters, s.unique = h.uniqueSort, s.text = h.getText, s.isXMLDoc = h.isXML, s.contains = h.contains
      }();
      var j = /Until$/,
          F = /^(?:parents|prevUntil|prevAll)/,
          I = /,/,
          q = /^.[^:#\[\.,]*$/,
          R = Array.prototype.slice,
          U = s.expr.match.POS,
          z = {
          children: !0,
          contents: !0,
          next: !0,
          prev: !0
          };
      s.fn.extend({
        find: function(e) {
          var t = this,
              n, r;
          if (typeof e != "string") return s(e).filter(function() {
            for (n = 0, r = t.length; n < r; n++) if (s.contains(t[n], this)) return !0
          });
          var i = this.pushStack("", "find", e),
              o, u, a;
          for (n = 0, r = this.length; n < r; n++) {
            o = i.length, s.find(e, this[n], i);
            if (n > 0) for (u = o; u < i.length; u++) for (a = 0; a < o; a++) if (i[a] === i[u]) {
              i.splice(u--, 1);
              break
            }
          }
          return i
        },
        has: function(e) {
          var t = s(e);
          return this.filter(function() {
            for (var e = 0, n = t.length; e < n; e++) if (s.contains(this, t[e])) return !0
          })
        },
        not: function(e) {
          return this.pushStack(X(this, e, !1), "not", e)
        },
        filter: function(e) {
          return this.pushStack(X(this, e, !0), "filter", e)
        },
        is: function(e) {
          return !!e && (typeof e == "string" ? U.test(e) ? s(e, this.context).index(this[0]) >= 0 : s.filter(e, this).length > 0 : this.filter(e).length > 0)
        },
        closest: function(e, t) {
          var n = [],
              r, i, o = this[0];
          if (s.isArray(e)) {
            var u = 1;
            while (o && o.ownerDocument && o !== t) {
              for (r = 0; r < e.length; r++) s(o).is(e[r]) && n.push({
                selector: e[r],
                elem: o,
                level: u
              });
              o = o.parentNode, u++
            }
            return n
          }
          var a = U.test(e) || typeof e != "string" ? s(e, t || this.context) : 0;
          for (r = 0, i = this.length; r < i; r++) {
            o = this[r];
            while (o) {
              if (a ? a.index(o) > -1 : s.find.matchesSelector(o, e)) {
                n.push(o);
                break
              }
              o = o.parentNode;
              if (!o || !o.ownerDocument || o === t || o.nodeType === 11) break
            }
          }
          return n = n.length > 1 ? s.unique(n) : n, this.pushStack(n, "closest", e)
        },
        index: function(e) {
          return e ? typeof e == "string" ? s.inArray(this[0], s(e)) : s.inArray(e.jquery ? e[0] : e, this) : this[0] && this[0].parentNode ? this.prevAll().length : -1
        },
        add: function(e, t) {
          var n = typeof e == "string" ? s(e, t) : s.makeArray(e && e.nodeType ? [e] : e),
              r = s.merge(this.get(), n);
          return this.pushStack(W(n[0]) || W(r[0]) ? r : s.unique(r))
        },
        andSelf: function() {
          return this.add(this.prevObject)
        }
      }), s.each({
        parent: function(e) {
          var t = e.parentNode;
          return t && t.nodeType !== 11 ? t : null
        },
        parents: function(e) {
          return s.dir(e, "parentNode")
        },
        parentsUntil: function(e, t, n) {
          return s.dir(e, "parentNode", n)
        },
        next: function(e) {
          return s.nth(e, 2, "nextSibling")
        },
        prev: function(e) {
          return s.nth(e, 2, "previousSibling")
        },
        nextAll: function(e) {
          return s.dir(e, "nextSibling")
        },
        prevAll: function(e) {
          return s.dir(e, "previousSibling")
        },
        nextUntil: function(e, t, n) {
          return s.dir(e, "nextSibling", n)
        },
        prevUntil: function(e, t, n) {
          return s.dir(e, "previousSibling", n)
        },
        siblings: function(e) {
          return s.sibling(e.parentNode.firstChild, e)
        },
        children: function(e) {
          return s.sibling(e.firstChild)
        },
        contents: function(e) {
          return s.nodeName(e, "iframe") ? e.contentDocument || e.contentWindow.document : s.makeArray(e.childNodes)
        }
      }, function(e, t) {
        s.fn[e] = function(n, r) {
          var i = s.map(this, t, n);
          return j.test(e) || (r = n), r && typeof r == "string" && (i = s.filter(r, i)), i = this.length > 1 && !z[e] ? s.unique(i) : i, (this.length > 1 || I.test(r)) && F.test(e) && (i = i.reverse()), this.pushStack(i, e, R.call(arguments).join(","))
        }
      }), s.extend({
        filter: function(e, t, n) {
          return n && (e = ":not(" + e + ")"), t.length === 1 ? s.find.matchesSelector(t[0], e) ? [t[0]] : [] : s.find.matches(e, t)
        },
        dir: function(e, n, r) {
          var i = [],
              o = e[n];
          while (o && o.nodeType !== 9 && (r === t || o.nodeType !== 1 || !s(o).is(r))) o.nodeType === 1 && i.push(o), o = o[n];
          return i
        },
        nth: function(e, t, n, r) {
          t = t || 1;
          var i = 0;
          for (; e; e = e[n]) if (e.nodeType === 1 && ++i === t) break;
          return e
        },
        sibling: function(e, t) {
          var n = [];
          for (; e; e = e.nextSibling) e.nodeType === 1 && e !== t && n.push(e);
          return n
        }
      });
      var $ = "abbr|article|aside|audio|canvas|datalist|details|figcaption|figure|footer|header|hgroup|mark|meter|nav|output|progress|section|summary|time|video",
          J = / jQuery\d+="(?:\d+|null)"/g,
          K = /^\s+/,
          Q = /<(?!area|br|col|embed|hr|img|input|link|meta|param)(([\w:]+)[^>]*)\/>/ig,
          G = /<([\w:]+)/,
          Y = /<tbody/i,
          Z = /<|&#?\w+;/,
          et = /<(?:script|style)/i,
          tt = /<(?:script|object|embed|option|style)/i,
          nt = new RegExp("<(?:" + $ + ")", "i"),
          rt = /checked\s*(?:[^=]|=\s*.checked.)/i,
          it = /\/(java|ecma)script/i,
          st = /^\s*<!(?:\[CDATA\[|\-\-)/,
          ot = {
          option: [1, "<select multiple='multiple'>", "</select>"],
          legend: [1, "<fieldset>", "</fieldset>"],
          thead: [1, "<table>", "</table>"],
          tr: [2, "<table><tbody>", "</tbody></table>"],
          td: [3, "<table><tbody><tr>", "</tr></tbody></table>"],
          col: [2, "<table><tbody></tbody><colgroup>", "</colgroup></table>"],
          area: [1, "<map>", "</map>"],
          _default: [0, "", ""]
          },
          ut = V(n);
      ot.optgroup = ot.option, ot.tbody = ot.tfoot = ot.colgroup = ot.caption = ot.thead, ot.th = ot.td, s.support.htmlSerialize || (ot._default = [1, "div<div>", "</div>"]), s.fn.extend({
        text: function(e) {
          return s.isFunction(e) ? this.each(function(t) {
            var n = s(this);
            n.text(e.call(this, t, n.text()))
          }) : typeof e != "object" && e !== t ? this.empty().append((this[0] && this[0].ownerDocument || n).createTextNode(e)) : s.text(this)
        },
        wrapAll: function(e) {
          if (s.isFunction(e)) return this.each(function(t) {
            s(this).wrapAll(e.call(this, t))
          });
          if (this[0]) {
            var t = s(e, this[0].ownerDocument).eq(0).clone(!0);
            this[0].parentNode && t.insertBefore(this[0]), t.map(function() {
              var e = this;
              while (e.firstChild && e.firstChild.nodeType === 1) e = e.firstChild;
              return e
            }).append(this)
          }
          return this
        },
        wrapInner: function(e) {
          return s.isFunction(e) ? this.each(function(t) {
            s(this).wrapInner(e.call(this, t))
          }) : this.each(function() {
            var t = s(this),
                n = t.contents();
            n.length ? n.wrapAll(e) : t.append(e)
          })
        },
        wrap: function(e) {
          var t = s.isFunction(e);
          return this.each(function(n) {
            s(this).wrapAll(t ? e.call(this, n) : e)
          })
        },
        unwrap: function() {
          return this.parent().each(function() {
            s.nodeName(this, "body") || s(this).replaceWith(this.childNodes)
          }).end()
        },
        append: function() {
          return this.domManip(arguments, !0, function(e) {
            this.nodeType === 1 && this.appendChild(e)
          })
        },
        prepend: function() {
          return this.domManip(arguments, !0, function(e) {
            this.nodeType === 1 && this.insertBefore(e, this.firstChild)
          })
        },
        before: function() {
          if (this[0] && this[0].parentNode) return this.domManip(arguments, !1, function(e) {
            this.parentNode.insertBefore(e, this)
          });
          if (arguments.length) {
            var e = s.clean(arguments);
            return e.push.apply(e, this.toArray()), this.pushStack(e, "before", arguments)
          }
        },
        after: function() {
          if (this[0] && this[0].parentNode) return this.domManip(arguments, !1, function(e) {
            this.parentNode.insertBefore(e, this.nextSibling)
          });
          if (arguments.length) {
            var e = this.pushStack(this, "after", arguments);
            return e.push.apply(e, s.clean(arguments)), e
          }
        },
        remove: function(e, t) {
          for (var n = 0, r;
          (r = this[n]) != null; n++) if (!e || s.filter(e, [r]).length)!t && r.nodeType === 1 && (s.cleanData(r.getElementsByTagName("*")), s.cleanData([r])), r.parentNode && r.parentNode.removeChild(r);
          return this
        },
        empty: function() {
          for (var e = 0, t;
          (t = this[e]) != null; e++) {
            t.nodeType === 1 && s.cleanData(t.getElementsByTagName("*"));
            while (t.firstChild) t.removeChild(t.firstChild)
          }
          return this
        },
        clone: function(e, t) {
          return e = e == null ? !1 : e, t = t == null ? e : t, this.map(function() {
            return s.clone(this, e, t)
          })
        },
        html: function(e) {
          if (e === t) return this[0] && this[0].nodeType === 1 ? this[0].innerHTML.replace(J, "") : null;
          if (typeof e == "string" && !et.test(e) && (s.support.leadingWhitespace || !K.test(e)) && !ot[(G.exec(e) || ["", ""])[1].toLowerCase()]) {
            e = e.replace(Q, "<$1></$2>");
            try {
              for (var n = 0, r = this.length; n < r; n++) this[n].nodeType === 1 && (s.cleanData(this[n].getElementsByTagName("*")), this[n].innerHTML = e)
            } catch (i) {
              this.empty().append(e)
            }
          } else s.isFunction(e) ? this.each(function(t) {
            var n = s(this);
            n.html(e.call(this, t, n.html()))
          }) : this.empty().append(e);
          return this
        },
        replaceWith: function(e) {
          return this[0] && this[0].parentNode ? s.isFunction(e) ? this.each(function(t) {
            var n = s(this),
                r = n.html();
            n.replaceWith(e.call(this, t, r))
          }) : (typeof e != "string" && (e = s(e).detach()), this.each(function() {
            var t = this.nextSibling,
                n = this.parentNode;
            s(this).remove(), t ? s(t).before(e) : s(n).append(e)
          })) : this.length ? this.pushStack(s(s.isFunction(e) ? e() : e), "replaceWith", e) : this
        },
        detach: function(e) {
          return this.remove(e, !0)
        },
        domManip: function(e, n, r) {
          var i, o, u, a, f = e[0],
              l = [];
          if (!s.support.checkClone && arguments.length === 3 && typeof f == "string" && rt.test(f)) return this.each(function() {
            s(this).domManip(e, n, r, !0)
          });
          if (s.isFunction(f)) return this.each(function(i) {
            var o = s(this);
            e[0] = f.call(this, i, n ? o.html() : t), o.domManip(e, n, r)
          });
          if (this[0]) {
            a = f && f.parentNode, s.support.parentNode && a && a.nodeType === 11 && a.childNodes.length === this.length ? i = {
              fragment: a
            } : i = s.buildFragment(e, this, l), u = i.fragment, u.childNodes.length === 1 ? o = u = u.firstChild : o = u.firstChild;
            if (o) {
              n = n && s.nodeName(o, "tr");
              for (var c = 0, h = this.length, p = h - 1; c < h; c++) r.call(n ? at(this[c], o) : this[c], i.cacheable || h > 1 && c < p ? s.clone(u, !0, !0) : u)
            }
            l.length && s.each(l, vt)
          }
          return this
        }
      }), s.buildFragment = function(e, t, r) {
        var i, o, u, a, f = e[0];
        return t && t[0] && (a = t[0].ownerDocument || t[0]), a.createDocumentFragment || (a = n), e.length === 1 && typeof f == "string" && f.length < 512 && a === n && f.charAt(0) === "<" && !tt.test(f) && (s.support.checkClone || !rt.test(f)) && (s.support.html5Clone || !nt.test(f)) && (o = !0, u = s.fragments[f], u && u !== 1 && (i = u)), i || (i = a.createDocumentFragment(), s.clean(e, a, i, r)), o && (s.fragments[f] = u ? i : 1), {
          fragment: i,
          cacheable: o
        }
      }, s.fragments = {}, s.each({
        appendTo: "append",
        prependTo: "prepend",
        insertBefore: "before",
        insertAfter: "after",
        replaceAll: "replaceWith"
      }, function(e, t) {
        s.fn[e] = function(n) {
          var r = [],
              i = s(n),
              o = this.length === 1 && this[0].parentNode;
          if (o && o.nodeType === 11 && o.childNodes.length === 1 && i.length === 1) return i[t](this[0]), this;
          for (var u = 0, a = i.length; u < a; u++) {
            var f = (u > 0 ? this.clone(!0) : this).get();
            s(i[u])[t](f), r = r.concat(f)
          }
          return this.pushStack(r, e, i.selector)
        }
      }), s.extend({
        clone: function(e, t, n) {
          var r, i, o, u = s.support.html5Clone || !nt.test("<" + e.nodeName) ? e.cloneNode(!0) : dt(e);
          if ((!s.support.noCloneEvent || !s.support.noCloneChecked) && (e.nodeType === 1 || e.nodeType === 11) && !s.isXMLDoc(e)) {
            lt(e, u), r = ct(e), i = ct(u);
            for (o = 0; r[o]; ++o) i[o] && lt(r[o], i[o])
          }
          if (t) {
            ft(e, u);
            if (n) {
              r = ct(e), i = ct(u);
              for (o = 0; r[o]; ++o) ft(r[o], i[o])
            }
          }
          return r = i = null, u
        },
        clean: function(e, t, r, i) {
          var o;
          t = t || n, typeof t.createElement == "undefined" && (t = t.ownerDocument || t[0] && t[0].ownerDocument || n);
          var u = [],
              a;
          for (var f = 0, l;
          (l = e[f]) != null; f++) {
            typeof l == "number" && (l += "");
            if (!l) continue;
            if (typeof l == "string") if (!Z.test(l)) l = t.createTextNode(l);
            else {
              l = l.replace(Q, "<$1></$2>");
              var c = (G.exec(l) || ["", ""])[1].toLowerCase(),
                  h = ot[c] || ot._default,
                  p = h[0],
                  d = t.createElement("div");
              t === n ? ut.appendChild(d) : V(t).appendChild(d), d.innerHTML = h[1] + l + h[2];
              while (p--) d = d.lastChild;
              if (!s.support.tbody) {
                var v = Y.test(l),
                    m = c === "table" && !v ? d.firstChild && d.firstChild.childNodes : h[1] === "<table>" && !v ? d.childNodes : [];
                for (a = m.length - 1; a >= 0; --a) s.nodeName(m[a], "tbody") && !m[a].childNodes.length && m[a].parentNode.removeChild(m[a])
              }!s.support.leadingWhitespace && K.test(l) && d.insertBefore(t.createTextNode(K.exec(l)[0]), d.firstChild), l = d.childNodes
            }
            var g;
            if (!s.support.appendChecked) if (l[0] && typeof(g = l.length) == "number") for (a = 0; a < g; a++) pt(l[a]);
            else pt(l);
            l.nodeType ? u.push(l) : u = s.merge(u, l)
          }
          if (r) {
            o = function(e) {
              return !e.type || it.test(e.type)
            };
            for (f = 0; u[f]; f++) if (i && s.nodeName(u[f], "script") && (!u[f].type || u[f].type.toLowerCase() === "text/javascript")) i.push(u[f].parentNode ? u[f].parentNode.removeChild(u[f]) : u[f]);
            else {
              if (u[f].nodeType === 1) {
                var y = s.grep(u[f].getElementsByTagName("script"), o);
                u.splice.apply(u, [f + 1, 0].concat(y))
              }
              r.appendChild(u[f])
            }
          }
          return u
        },
        cleanData: function(e) {
          var t, n, r = s.cache,
              i = s.event.special,
              o = s.support.deleteExpando;
          for (var u = 0, a;
          (a = e[u]) != null; u++) {
            if (a.nodeName && s.noData[a.nodeName.toLowerCase()]) continue;
            n = a[s.expando];
            if (n) {
              t = r[n];
              if (t && t.events) {
                for (var f in t.events) i[f] ? s.event.remove(a, f) : s.removeEvent(a, f, t.handle);
                t.handle && (t.handle.elem = null)
              }
              o ? delete a[s.expando] : a.removeAttribute && a.removeAttribute(s.expando), delete r[n]
            }
          }
        }
      });
      var mt = /alpha\([^)]*\)/i,
          gt = /opacity=([^)]*)/,
          yt = /([A-Z]|^ms)/g,
          bt = /^-?\d+(?:px)?$/i,
          wt = /^-?\d/,
          Et = /^([\-+])=([\-+.\de]+)/,
          St = {
          position: "absolute",
          visibility: "hidden",
          display: "block"
          },
          xt = ["Left", "Right"],
          Tt = ["Top", "Bottom"],
          Nt, Ct, kt;
      s.fn.css = function(e, n) {
        return arguments.length === 2 && n === t ? this : s.access(this, e, n, !0, function(e, n, r) {
          return r !== t ? s.style(e, n, r) : s.css(e, n)
        })
      }, s.extend({
        cssHooks: {
          opacity: {
            get: function(e, t) {
              if (t) {
                var n = Nt(e, "opacity", "opacity");
                return n === "" ? "1" : n
              }
              return e.style.opacity
            }
          }
        },
        cssNumber: {
          fillOpacity: !0,
          fontWeight: !0,
          lineHeight: !0,
          opacity: !0,
          orphans: !0,
          widows: !0,
          zIndex: !0,
          zoom: !0
        },
        cssProps: {
          "float": s.support.cssFloat ? "cssFloat" : "styleFloat"
        },
        style: function(e, n, r, i) {
          if (!e || e.nodeType === 3 || e.nodeType === 8 || !e.style) return;
          var o, u, a = s.camelCase(n),
              f = e.style,
              l = s.cssHooks[a];
          n = s.cssProps[a] || a;
          if (r === t) return l && "get" in l && (o = l.get(e, !1, i)) !== t ? o : f[n];
          u = typeof r, u === "string" && (o = Et.exec(r)) && (r = +(o[1] + 1) * +o[2] + parseFloat(s.css(e, n)), u = "number");
          if (r == null || u === "number" && isNaN(r)) return;
          u === "number" && !s.cssNumber[a] && (r += "px");
          if (!l || !("set" in l) || (r = l.set(e, r)) !== t) try {
            f[n] = r
          } catch (c) {}
        },
        css: function(e, n, r) {
          var i, o;
          n = s.camelCase(n), o = s.cssHooks[n], n = s.cssProps[n] || n, n === "cssFloat" && (n = "float");
          if (o && "get" in o && (i = o.get(e, !0, r)) !== t) return i;
          if (Nt) return Nt(e, n)
        },
        swap: function(e, t, n) {
          var r = {};
          for (var i in t) r[i] = e.style[i], e.style[i] = t[i];
          n.call(e);
          for (i in t) e.style[i] = r[i]
        }
      }), s.curCSS = s.css, s.each(["height", "width"], function(e, t) {
        s.cssHooks[t] = {
          get: function(e, n, r) {
            var i;
            if (n) return e.offsetWidth !== 0 ? Lt(e, t, r) : (s.swap(e, St, function() {
              i = Lt(e, t, r)
            }), i)
          },
          set: function(e, t) {
            if (!bt.test(t)) return t;
            t = parseFloat(t);
            if (t >= 0) return t + "px"
          }
        }
      }), s.support.opacity || (s.cssHooks.opacity = {
        get: function(e, t) {
          return gt.test((t && e.currentStyle ? e.currentStyle.filter : e.style.filter) || "") ? parseFloat(RegExp.$1) / 100 + "" : t ? "1" : ""
        },
        set: function(e, t) {
          var n = e.style,
              r = e.currentStyle,
              i = s.isNumeric(t) ? "alpha(opacity=" + t * 100 + ")" : "",
              o = r && r.filter || n.filter || "";
          n.zoom = 1;
          if (t >= 1 && s.trim(o.replace(mt, "")) === "") {
            n.removeAttribute("filter");
            if (r && !r.filter) return
          }
          n.filter = mt.test(o) ? o.replace(mt, i) : o + " " + i
        }
      }), s(function() {
        s.support.reliableMarginRight || (s.cssHooks.marginRight = {
          get: function(e, t) {
            var n;
            return s.swap(e, {
              display: "inline-block"
            }, function() {
              t ? n = Nt(e, "margin-right", "marginRight") : n = e.style.marginRight
            }), n
          }
        })
      }), n.defaultView && n.defaultView.getComputedStyle && (Ct = function(e, t) {
        var n, r, i;
        return t = t.replace(yt, "-$1").toLowerCase(), (r = e.ownerDocument.defaultView) && (i = r.getComputedStyle(e, null)) && (n = i.getPropertyValue(t), n === "" && !s.contains(e.ownerDocument.documentElement, e) && (n = s.style(e, t))), n
      }), n.documentElement.currentStyle && (kt = function(e, t) {
        var n, r, i, s = e.currentStyle && e.currentStyle[t],
            o = e.style;
        return s === null && o && (i = o[t]) && (s = i), !bt.test(s) && wt.test(s) && (n = o.left, r = e.runtimeStyle && e.runtimeStyle.left, r && (e.runtimeStyle.left = e.currentStyle.left), o.left = t === "fontSize" ? "1em" : s || 0, s = o.pixelLeft + "px", o.left = n, r && (e.runtimeStyle.left = r)), s === "" ? "auto" : s
      }), Nt = Ct || kt, s.expr && s.expr.filters && (s.expr.filters.hidden = function(e) {
        var t = e.offsetWidth,
            n = e.offsetHeight;
        return t === 0 && n === 0 || !s.support.reliableHiddenOffsets && (e.style && e.style.display || s.css(e, "display")) === "none"
      }, s.expr.filters.visible = function(e) {
        return !s.expr.filters.hidden(e)
      });
      var At = /%20/g,
          Ot = /\[\]$/,
          Mt = /\r?\n/g,
          _t = /#.*$/,
          Dt = /^(.*?):[ \t]*([^\r\n]*)\r?$/mg,
          Pt = /^(?:color|date|datetime|datetime-local|email|hidden|month|number|password|range|search|tel|text|time|url|week)$/i,
          Ht = /^(?:about|app|app\-storage|.+\-extension|file|res|widget):$/,
          Bt = /^(?:GET|HEAD)$/,
          jt = /^\/\//,
          Ft = /\?/,
          It = /<script\b[^<]*(?:(?!<\/script>)<[^<]*)*<\/script>/gi,
          qt = /^(?:select|textarea)/i,
          Rt = /\s+/,
          Ut = /([?&])_=[^&]*/,
          zt = /^([\w\+\.\-]+:)(?:\/\/([^\/?#:]*)(?::(\d+))?)?/,
          Wt = s.fn.load,
          Xt = {},
          Vt = {},
          $t, Jt, Kt = ["*/"] + ["*"];
      try {
        $t = i.href
      } catch (Qt) {
        $t = n.createElement("a"), $t.href = "", $t = $t.href
      }
      Jt = zt.exec($t.toLowerCase()) || [], s.fn.extend({
        load: function(e, n, r) {
          if (typeof e != "string" && Wt) return Wt.apply(this, arguments);
          if (!this.length) return this;
          var i = e.indexOf(" ");
          if (i >= 0) {
            var o = e.slice(i, e.length);
            e = e.slice(0, i)
          }
          var u = "GET";
          n && (s.isFunction(n) ? (r = n, n = t) : typeof n == "object" && (n = s.param(n, s.ajaxSettings.traditional), u = "POST"));
          var a = this;
          return s.ajax({
            url: e,
            type: u,
            dataType: "html",
            data: n,
            complete: function(e, t, n) {
              n = e.responseText, e.isResolved() && (e.done(function(e) {
                n = e
              }), a.html(o ? s("<div>").append(n.replace(It, "")).find(o) : n)), r && a.each(r, [n, t, e])
            }
          }), this
        },
        serialize: function() {
          return s.param(this.serializeArray())
        },
        serializeArray: function() {
          return this.map(function() {
            return this.elements ? s.makeArray(this.elements) : this
          }).filter(function() {
            return this.name && !this.disabled && (this.checked || qt.test(this.nodeName) || Pt.test(this.type))
          }).map(function(e, t) {
            var n = s(this).val();
            return n == null ? null : s.isArray(n) ? s.map(n, function(e, n) {
              return {
                name: t.name,
                value: e.replace(Mt, "\r\n")
              }
            }) : {
              name: t.name,
              value: n.replace(Mt, "\r\n")
            }
          }).get()
        }
      }), s.each("ajaxStart ajaxStop ajaxComplete ajaxError ajaxSuccess ajaxSend".split(" "), function(e, t) {
        s.fn[t] = function(e) {
          return this.on(t, e)
        }
      }), s.each(["get", "post"], function(e, n) {
        s[n] = function(e, r, i, o) {
          return s.isFunction(r) && (o = o || i, i = r, r = t), s.ajax({
            type: n,
            url: e,
            data: r,
            success: i,
            dataType: o
          })
        }
      }), s.extend({
        getScript: function(e, n) {
          return s.get(e, t, n, "script")
        },
        getJSON: function(e, t, n) {
          return s.get(e, t, n, "json")
        },
        ajaxSetup: function(e, t) {
          return t ? Zt(e, s.ajaxSettings) : (t = e, e = s.ajaxSettings), Zt(e, t), e
        },
        ajaxSettings: {
          url: $t,
          isLocal: Ht.test(Jt[1]),
          global: !0,
          type: "GET",
          contentType: "application/x-www-form-urlencoded",
          processData: !0,
          async: !0,
          accepts: {
            xml: "application/xml, text/xml",
            html: "text/html",
            text: "text/plain",
            json: "application/json, text/javascript",
            "*": Kt
          },
          contents: {
            xml: /xml/,
            html: /html/,
            json: /json/
          },
          responseFields: {
            xml: "responseXML",
            text: "responseText"
          },
          converters: {
            "* text": e.String,
            "text html": !0,
            "text json": s.parseJSON,
            "text xml": s.parseXML
          },
          flatOptions: {
            context: !0,
            url: !0
          }
        },
        ajaxPrefilter: Gt(Xt),
        ajaxTransport: Gt(Vt),
        ajax: function(e, n) {
          function S(e, n, c, h) {
            if (y === 2) return;
            y = 2, m && clearTimeout(m), v = t, p = h || "", E.readyState = e > 0 ? 4 : 0;
            var d, g, w, S = n,
                x = c ? tn(r, E, c) : t,
                T, N;
            if (e >= 200 && e < 300 || e === 304) {
              if (r.ifModified) {
                if (T = E.getResponseHeader("Last-Modified")) s.lastModified[l] = T;
                if (N = E.getResponseHeader("Etag")) s.etag[l] = N
              }
              if (e === 304) S = "notmodified", d = !0;
              else try {
                g = nn(r, x), S = "success", d = !0
              } catch (C) {
                S = "parsererror", w = C
              }
            } else {
              w = S;
              if (!S || e) S = "error", e < 0 && (e = 0)
            }
            E.status = e, E.statusText = "" + (n || S), d ? u.resolveWith(i, [g, S, E]) : u.rejectWith(i, [E, S, w]), E.statusCode(f), f = t, b && o.trigger("ajax" + (d ? "Success" : "Error"), [E, r, d ? g : w]), a.fireWith(i, [E, S]), b && (o.trigger("ajaxComplete", [E, r]), --s.active || s.event.trigger("ajaxStop"))
          }
          typeof e == "object" && (n = e, e = t), n = n || {};
          var r = s.ajaxSetup({}, n),
              i = r.context || r,
              o = i !== r && (i.nodeType || i instanceof s) ? s(i) : s.event,
              u = s.Deferred(),
              a = s.Callbacks("once memory"),
              f = r.statusCode || {},
              l, c = {},
              h = {},
              p, d, v, m, g, y = 0,
              b, w, E = {
              readyState: 0,
              setRequestHeader: function(e, t) {
                if (!y) {
                  var n = e.toLowerCase();
                  e = h[n] = h[n] || e, c[e] = t
                }
                return this
              },
              getAllResponseHeaders: function() {
                return y === 2 ? p : null
              },
              getResponseHeader: function(e) {
                var n;
                if (y === 2) {
                  if (!d) {
                    d = {};
                    while (n = Dt.exec(p)) d[n[1].toLowerCase()] = n[2]
                  }
                  n = d[e.toLowerCase()]
                }
                return n === t ? null : n
              },
              overrideMimeType: function(e) {
                return y || (r.mimeType = e), this
              },
              abort: function(e) {
                return e = e || "abort", v && v.abort(e), S(0, e), this
              }
              };
          u.promise(E), E.success = E.done, E.error = E.fail, E.complete = a.add, E.statusCode = function(e) {
            if (e) {
              var t;
              if (y < 2) for (t in e) f[t] = [f[t], e[t]];
              else t = e[E.status], E.then(t, t)
            }
            return this
          }, r.url = ((e || r.url) + "").replace(_t, "").replace(jt, Jt[1] + "//"), r.dataTypes = s.trim(r.dataType || "*").toLowerCase().split(Rt), r.crossDomain == null && (g = zt.exec(r.url.toLowerCase()), r.crossDomain = !(!g || g[1] == Jt[1] && g[2] == Jt[2] && (g[3] || (g[1] === "http:" ? 80 : 443)) == (Jt[3] || (Jt[1] === "http:" ? 80 : 443)))), r.data && r.processData && typeof r.data != "string" && (r.data = s.param(r.data, r.traditional)), Yt(Xt, r, n, E);
          if (y === 2) return !1;
          b = r.global, r.type = r.type.toUpperCase(), r.hasContent = !Bt.test(r.type), b && s.active++ === 0 && s.event.trigger("ajaxStart");
          if (!r.hasContent) {
            r.data && (r.url += (Ft.test(r.url) ? "&" : "?") + r.data, delete r.data), l = r.url;
            if (r.cache === !1) {
              var x = s.now(),
                  T = r.url.replace(Ut, "$1_=" + x);
              r.url = T + (T === r.url ? (Ft.test(r.url) ? "&" : "?") + "_=" + x : "")
            }
          }(r.data && r.hasContent && r.contentType !== !1 || n.contentType) && E.setRequestHeader("Content-Type", r.contentType), r.ifModified && (l = l || r.url, s.lastModified[l] && E.setRequestHeader("If-Modified-Since", s.lastModified[l]), s.etag[l] && E.setRequestHeader("If-None-Match", s.etag[l])), E.setRequestHeader("Accept", r.dataTypes[0] && r.accepts[r.dataTypes[0]] ? r.accepts[r.dataTypes[0]] + (r.dataTypes[0] !== "*" ? ", " + Kt + "; q=0.01" : "") : r.accepts["*"]);
          for (w in r.headers) E.setRequestHeader(w, r.headers[w]);
          if (!r.beforeSend || r.beforeSend.call(i, E, r) !== !1 && y !== 2) {
            for (w in {
              success: 1,
              error: 1,
              complete: 1
            }) E[w](r[w]);
            v = Yt(Vt, r, n, E);
            if (!v) S(-1, "No Transport");
            else {
              E.readyState = 1, b && o.trigger("ajaxSend", [E, r]), r.async && r.timeout > 0 && (m = setTimeout(function() {
                E.abort("timeout")
              }, r.timeout));
              try {
                y = 1, v.send(c, S)
              } catch (N) {
                if (!(y < 2)) throw N;
                S(-1, N)
              }
            }
            return E
          }
          return E.abort(), !1
        },
        param: function(e, n) {
          var r = [],
              i = function(e, t) {
              t = s.isFunction(t) ? t() : t, r[r.length] = encodeURIComponent(e) + "=" + encodeURIComponent(t)
              };
          n === t && (n = s.ajaxSettings.traditional);
          if (s.isArray(e) || e.jquery && !s.isPlainObject(e)) s.each(e, function() {
            i(this.name, this.value)
          });
          else for (var o in e) en(o, e[o], n, i);
          return r.join("&").replace(At, "+")
        }
      }), s.extend({
        active: 0,
        lastModified: {},
        etag: {}
      });
      var rn = s.now(),
          sn = /(\=)\?(&|$)|\?\?/i;
      s.ajaxSetup({
        jsonp: "callback",
        jsonpCallback: function() {
          return s.expando + "_" + rn++
        }
      }), s.ajaxPrefilter("json jsonp", function(t, n, r) {
        var i = t.contentType === "application/x-www-form-urlencoded" && typeof t.data == "string";
        if (t.dataTypes[0] === "jsonp" || t.jsonp !== !1 && (sn.test(t.url) || i && sn.test(t.data))) {
          var o, u = t.jsonpCallback = s.isFunction(t.jsonpCallback) ? t.jsonpCallback() : t.jsonpCallback,
              a = e[u],
              f = t.url,
              l = t.data,
              c = "$1" + u + "$2";
          return t.jsonp !== !1 && (f = f.replace(sn, c), t.url === f && (i && (l = l.replace(sn, c)), t.data === l && (f += (/\?/.test(f) ? "&" : "?") + t.jsonp + "=" + u))), t.url = f, t.data = l, e[u] = function(e) {
            o = [e]
          }, r.always(function() {
            e[u] = a, o && s.isFunction(a) && e[u](o[0])
          }), t.converters["script json"] = function() {
            return o || s.error(u + " was not called"), o[0]
          }, t.dataTypes[0] = "json", "script"
        }
      }), s.ajaxSetup({
        accepts: {
          script: "text/javascript, application/javascript, application/ecmascript, application/x-ecmascript"
        },
        contents: {
          script: /javascript|ecmascript/
        },
        converters: {
          "text script": function(e) {
            return s.globalEval(e), e
          }
        }
      }), s.ajaxPrefilter("script", function(e) {
        e.cache === t && (e.cache = !1), e.crossDomain && (e.type = "GET", e.global = !1)
      }), s.ajaxTransport("script", function(e) {
        if (e.crossDomain) {
          var r, i = n.head || n.getElementsByTagName("head")[0] || n.documentElement;
          return {
            send: function(s, o) {
              r = n.createElement("script"), r.async = "async", e.scriptCharset && (r.charset = e.scriptCharset), r.src = e.url, r.onload = r.onreadystatechange = function(e, n) {
                if (n || !r.readyState || /loaded|complete/.test(r.readyState)) r.onload = r.onreadystatechange = null, i && r.parentNode && i.removeChild(r), r = t, n || o(200, "success")
              }, i.insertBefore(r, i.firstChild)
            },
            abort: function() {
              r && r.onload(0, 1)
            }
          }
        }
      });
      var on = e.ActiveXObject ?
      function() {
        for (var e in an) an[e](0, 1)
      } : !1, un = 0, an;
      s.ajaxSettings.xhr = e.ActiveXObject ?
      function() {
        return !this.isLocal && fn() || ln()
      } : fn, function(e) {
        s.extend(s.support, {
          ajax: !! e,
          cors: !! e && "withCredentials" in e
        })
      }(s.ajaxSettings.xhr()), s.support.ajax && s.ajaxTransport(function(n) {
        if (!n.crossDomain || s.support.cors) {
          var r;
          return {
            send: function(i, o) {
              var u = n.xhr(),
                  a, f;
              n.username ? u.open(n.type, n.url, n.async, n.username, n.password) : u.open(n.type, n.url, n.async);
              if (n.xhrFields) for (f in n.xhrFields) u[f] = n.xhrFields[f];
              n.mimeType && u.overrideMimeType && u.overrideMimeType(n.mimeType), !n.crossDomain && !i["X-Requested-With"] && (i["X-Requested-With"] = "XMLHttpRequest");
              try {
                for (f in i) u.setRequestHeader(f, i[f])
              } catch (l) {}
              u.send(n.hasContent && n.data || null), r = function(e, i) {
                var f, l, c, h, p;
                try {
                  if (r && (i || u.readyState === 4)) {
                    r = t, a && (u.onreadystatechange = s.noop, on && delete an[a]);
                    if (i) u.readyState !== 4 && u.abort();
                    else {
                      f = u.status, c = u.getAllResponseHeaders(), h = {}, p = u.responseXML, p && p.documentElement && (h.xml = p), h.text = u.responseText;
                      try {
                        l = u.statusText
                      } catch (d) {
                        l = ""
                      }!f && n.isLocal && !n.crossDomain ? f = h.text ? 200 : 404 : f === 1223 && (f = 204)
                    }
                  }
                } catch (v) {
                  i || o(-1, v)
                }
                h && o(f, l, h, c)
              }, !n.async || u.readyState === 4 ? r() : (a = ++un, on && (an || (an = {}, s(e).unload(on)), an[a] = r), u.onreadystatechange = r)
            },
            abort: function() {
              r && r(0, 1)
            }
          }
        }
      });
      var cn = {},
          hn, pn, dn = /^(?:toggle|show|hide)$/,
          vn = /^([+\-]=)?([\d+.\-]+)([a-z%]*)$/i,
          mn, gn = [
          ["height", "marginTop", "marginBottom", "paddingTop", "paddingBottom"],
          ["width", "marginLeft", "marginRight", "paddingLeft", "paddingRight"],
          ["opacity"]
          ],
          yn;
      s.fn.extend({
        show: function(e, t, n) {
          var r, i;
          if (e || e === 0) return this.animate(En("show", 3), e, t, n);
          for (var o = 0, u = this.length; o < u; o++) r = this[o], r.style && (i = r.style.display, !s._data(r, "olddisplay") && i === "none" && (i = r.style.display = ""), i === "" && s.css(r, "display") === "none" && s._data(r, "olddisplay", Sn(r.nodeName)));
          for (o = 0; o < u; o++) {
            r = this[o];
            if (r.style) {
              i = r.style.display;
              if (i === "" || i === "none") r.style.display = s._data(r, "olddisplay") || ""
            }
          }
          return this
        },
        hide: function(e, t, n) {
          if (e || e === 0) return this.animate(En("hide", 3), e, t, n);
          var r, i, o = 0,
              u = this.length;
          for (; o < u; o++) r = this[o], r.style && (i = s.css(r, "display"), i !== "none" && !s._data(r, "olddisplay") && s._data(r, "olddisplay", i));
          for (o = 0; o < u; o++) this[o].style && (this[o].style.display = "none");
          return this
        },
        _toggle: s.fn.toggle,
        toggle: function(e, t, n) {
          var r = typeof e == "boolean";
          return s.isFunction(e) && s.isFunction(t) ? this._toggle.apply(this, arguments) : e == null || r ? this.each(function() {
            var t = r ? e : s(this).is(":hidden");
            s(this)[t ? "show" : "hide"]()
          }) : this.animate(En("toggle", 3), e, t, n), this
        },
        fadeTo: function(e, t, n, r) {
          return this.filter(":hidden").css("opacity", 0).show().end().animate({
            opacity: t
          }, e, n, r)
        },
        animate: function(e, t, n, r) {
          function o() {
            i.queue === !1 && s._mark(this);
            var t = s.extend({}, i),
                n = this.nodeType === 1,
                r = n && s(this).is(":hidden"),
                o, u, a, f, l, c, h, p, d;
            t.animatedProperties = {};
            for (a in e) {
              o = s.camelCase(a), a !== o && (e[o] = e[a], delete e[a]), u = e[o], s.isArray(u) ? (t.animatedProperties[o] = u[1], u = e[o] = u[0]) : t.animatedProperties[o] = t.specialEasing && t.specialEasing[o] || t.easing || "swing";
              if (u === "hide" && r || u === "show" && !r) return t.complete.call(this);
              n && (o === "height" || o === "width") && (t.overflow = [this.style.overflow, this.style.overflowX, this.style.overflowY], s.css(this, "display") === "inline" && s.css(this, "float") === "none" && (!s.support.inlineBlockNeedsLayout || Sn(this.nodeName) === "inline" ? this.style.display = "inline-block" : this.style.zoom = 1))
            }
            t.overflow != null && (this.style.overflow = "hidden");
            for (a in e) f = new s.fx(this, t, a), u = e[a], dn.test(u) ? (d = s._data(this, "toggle" + a) || (u === "toggle" ? r ? "show" : "hide" : 0), d ? (s._data(this, "toggle" + a, d === "show" ? "hide" : "show"), f[d]()) : f[u]()) : (l = vn.exec(u), c = f.cur(), l ? (h = parseFloat(l[2]), p = l[3] || (s.cssNumber[a] ? "" : "px"), p !== "px" && (s.style(this, a, (h || 1) + p), c = (h || 1) / f.cur() * c, s.style(this, a, c + p)), l[1] && (h = (l[1] === "-=" ? -1 : 1) * h + c), f.custom(c, h, p)) : f.custom(c, u, ""));
            return !0
          }
          var i = s.speed(t, n, r);
          return s.isEmptyObject(e) ? this.each(i.complete, [!1]) : (e = s.extend({}, e), i.queue === !1 ? this.each(o) : this.queue(i.queue, o))
        },
        stop: function(e, n, r) {
          return typeof e != "string" && (r = n, n = e, e = t), n && e !== !1 && this.queue(e || "fx", []), this.each(function() {
            function u(e, t, n) {
              var i = t[n];
              s.removeData(e, n, !0), i.stop(r)
            }
            var t, n = !1,
                i = s.timers,
                o = s._data(this);
            r || s._unmark(!0, this);
            if (e == null) for (t in o) o[t] && o[t].stop && t.indexOf(".run") === t.length - 4 && u(this, o, t);
            else o[t = e + ".run"] && o[t].stop && u(this, o, t);
            for (t = i.length; t--;) i[t].elem === this && (e == null || i[t].queue === e) && (r ? i[t](!0) : i[t].saveState(), n = !0, i.splice(t, 1));
            (!r || !n) && s.dequeue(this, e)
          })
        }
      }), s.each({
        slideDown: En("show", 1),
        slideUp: En("hide", 1),
        slideToggle: En("toggle", 1),
        fadeIn: {
          opacity: "show"
        },
        fadeOut: {
          opacity: "hide"
        },
        fadeToggle: {
          opacity: "toggle"
        }
      }, function(e, t) {
        s.fn[e] = function(e, n, r) {
          return this.animate(t, e, n, r)
        }
      }), s.extend({
        speed: function(e, t, n) {
          var r = e && typeof e == "object" ? s.extend({}, e) : {
            complete: n || !n && t || s.isFunction(e) && e,
            duration: e,
            easing: n && t || t && !s.isFunction(t) && t
          };
          r.duration = s.fx.off ? 0 : typeof r.duration == "number" ? r.duration : r.duration in s.fx.speeds ? s.fx.speeds[r.duration] : s.fx.speeds._default;
          if (r.queue == null || r.queue === !0) r.queue = "fx";
          return r.old = r.complete, r.complete = function(e) {
            s.isFunction(r.old) && r.old.call(this), r.queue ? s.dequeue(this, r.queue) : e !== !1 && s._unmark(this)
          }, r
        },
        easing: {
          linear: function(e, t, n, r) {
            return n + r * e
          },
          swing: function(e, t, n, r) {
            return (-Math.cos(e * Math.PI) / 2 + .5) * r + n
          }
        },
        timers: [],
        fx: function(e, t, n) {
          this.options = t, this.elem = e, this.prop = n, t.orig = t.orig || {}
        }
      }), s.fx.prototype = {
        update: function() {
          this.options.step && this.options.step.call(this.elem, this.now, this), (s.fx.step[this.prop] || s.fx.step._default)(this)
        },
        cur: function() {
          if (this.elem[this.prop] == null || !! this.elem.style && this.elem.style[this.prop] != null) {
            var e, t = s.css(this.elem, this.prop);
            return isNaN(e = parseFloat(t)) ? !t || t === "auto" ? 0 : t : e
          }
          return this.elem[this.prop]
        },
        custom: function(e, n, r) {
          function u(e) {
            return i.step(e)
          }
          var i = this,
              o = s.fx;
          this.startTime = yn || bn(), this.end = n, this.now = this.start = e, this.pos = this.state = 0, this.unit = r || this.unit || (s.cssNumber[this.prop] ? "" : "px"), u.queue = this.options.queue, u.elem = this.elem, u.saveState = function() {
            i.options.hide && s._data(i.elem, "fxshow" + i.prop) === t && s._data(i.elem, "fxshow" + i.prop, i.start)
          }, u() && s.timers.push(u) && !mn && (mn = setInterval(o.tick, o.interval))
        },
        show: function() {
          var e = s._data(this.elem, "fxshow" + this.prop);
          this.options.orig[this.prop] = e || s.style(this.elem, this.prop), this.options.show = !0, e !== t ? this.custom(this.cur(), e) : this.custom(this.prop === "width" || this.prop === "height" ? 1 : 0, this.cur()), s(this.elem).show()
        },
        hide: function() {
          this.options.orig[this.prop] = s._data(this.elem, "fxshow" + this.prop) || s.style(this.elem, this.prop), this.options.hide = !0, this.custom(this.cur(), 0)
        },
        step: function(e) {
          var t, n, r, i = yn || bn(),
              o = !0,
              u = this.elem,
              a = this.options;
          if (e || i >= a.duration + this.startTime) {
            this.now = this.end, this.pos = this.state = 1, this.update(), a.animatedProperties[this.prop] = !0;
            for (t in a.animatedProperties) a.animatedProperties[t] !== !0 && (o = !1);
            if (o) {
              a.overflow != null && !s.support.shrinkWrapBlocks && s.each(["", "X", "Y"], function(e, t) {
                u.style["overflow" + t] = a.overflow[e]
              }), a.hide && s(u).hide();
              if (a.hide || a.show) for (t in a.animatedProperties) s.style(u, t, a.orig[t]), s.removeData(u, "fxshow" + t, !0), s.removeData(u, "toggle" + t, !0);
              r = a.complete, r && (a.complete = !1, r.call(u))
            }
            return !1
          }
          return a.duration == Infinity ? this.now = i : (n = i - this.startTime, this.state = n / a.duration, this.pos = s.easing[a.animatedProperties[this.prop]](this.state, n, 0, 1, a.duration), this.now = this.start + (this.end - this.start) * this.pos), this.update(), !0
        }
      }, s.extend(s.fx, {
        tick: function() {
          var e, t = s.timers,
              n = 0;
          for (; n < t.length; n++) e = t[n], !e() && t[n] === e && t.splice(n--, 1);
          t.length || s.fx.stop()
        },
        interval: 13,
        stop: function() {
          clearInterval(mn), mn = null
        },
        speeds: {
          slow: 600,
          fast: 200,
          _default: 400
        },
        step: {
          opacity: function(e) {
            s.style(e.elem, "opacity", e.now)
          },
          _default: function(e) {
            e.elem.style && e.elem.style[e.prop] != null ? e.elem.style[e.prop] = e.now + e.unit : e.elem[e.prop] = e.now
          }
        }
      }), s.each(["width", "height"], function(e, t) {
        s.fx.step[t] = function(e) {
          s.style(e.elem, t, Math.max(0, e.now) + e.unit)
        }
      }), s.expr && s.expr.filters && (s.expr.filters.animated = function(e) {
        return s.grep(s.timers, function(t) {
          return e === t.elem
        }).length
      });
      var xn = /^t(?:able|d|h)$/i,
          Tn = /^(?:body|html)$/i;
      "getBoundingClientRect" in n.documentElement ? s.fn.offset = function(e) {
        var t = this[0],
            n;
        if (e) return this.each(function(t) {
          s.offset.setOffset(this, e, t)
        });
        if (!t || !t.ownerDocument) return null;
        if (t === t.ownerDocument.body) return s.offset.bodyOffset(t);
        try {
          n = t.getBoundingClientRect()
        } catch (r) {}
        var i = t.ownerDocument,
            o = i.documentElement;
        if (!n || !s.contains(o, t)) return n ? {
          top: n.top,
          left: n.left
        } : {
          top: 0,
          left: 0
        };
        var u = i.body,
            a = Nn(i),
            f = o.clientTop || u.clientTop || 0,
            l = o.clientLeft || u.clientLeft || 0,
            c = a.pageYOffset || s.support.boxModel && o.scrollTop || u.scrollTop,
            h = a.pageXOffset || s.support.boxModel && o.scrollLeft || u.scrollLeft,
            p = n.top + c - f,
            d = n.left + h - l;
        return {
          top: p,
          left: d
        }
      } : s.fn.offset = function(e) {
        var t = this[0];
        if (e) return this.each(function(t) {
          s.offset.setOffset(this, e, t)
        });
        if (!t || !t.ownerDocument) return null;
        if (t === t.ownerDocument.body) return s.offset.bodyOffset(t);
        var n, r = t.offsetParent,
            i = t,
            o = t.ownerDocument,
            u = o.documentElement,
            a = o.body,
            f = o.defaultView,
            l = f ? f.getComputedStyle(t, null) : t.currentStyle,
            c = t.offsetTop,
            h = t.offsetLeft;
        while ((t = t.parentNode) && t !== a && t !== u) {
          if (s.support.fixedPosition && l.position === "fixed") break;
          n = f ? f.getComputedStyle(t, null) : t.currentStyle, c -= t.scrollTop, h -= t.scrollLeft, t === r && (c += t.offsetTop, h += t.offsetLeft, s.support.doesNotAddBorder && (!s.support.doesAddBorderForTableAndCells || !xn.test(t.nodeName)) && (c += parseFloat(n.borderTopWidth) || 0, h += parseFloat(n.borderLeftWidth) || 0), i = r, r = t.offsetParent), s.support.subtractsBorderForOverflowNotVisible && n.overflow !== "visible" && (c += parseFloat(n.borderTopWidth) || 0, h += parseFloat(n.borderLeftWidth) || 0), l = n
        }
        if (l.position === "relative" || l.position === "static") c += a.offsetTop, h += a.offsetLeft;
        return s.support.fixedPosition && l.position === "fixed" && (c += Math.max(u.scrollTop, a.scrollTop), h += Math.max(u.scrollLeft, a.scrollLeft)), {
          top: c,
          left: h
        }
      }, s.offset = {
        bodyOffset: function(e) {
          var t = e.offsetTop,
              n = e.offsetLeft;
          return s.support.doesNotIncludeMarginInBodyOffset && (t += parseFloat(s.css(e, "marginTop")) || 0, n += parseFloat(s.css(e, "marginLeft")) || 0), {
            top: t,
            left: n
          }
        },
        setOffset: function(e, t, n) {
          var r = s.css(e, "position");
          r === "static" && (e.style.position = "relative");
          var i = s(e),
              o = i.offset(),
              u = s.css(e, "top"),
              a = s.css(e, "left"),
              f = (r === "absolute" || r === "fixed") && s.inArray("auto", [u, a]) > -1,
              l = {},
              c = {},
              h, p;
          f ? (c = i.position(), h = c.top, p = c.left) : (h = parseFloat(u) || 0, p = parseFloat(a) || 0), s.isFunction(t) && (t = t.call(e, n, o)), t.top != null && (l.top = t.top - o.top + h), t.left != null && (l.left = t.left - o.left + p), "using" in t ? t.using.call(e, l) : i.css(l)
        }
      }, s.fn.extend({
        position: function() {
          if (!this[0]) return null;
          var e = this[0],
              t = this.offsetParent(),
              n = this.offset(),
              r = Tn.test(t[0].nodeName) ? {
              top: 0,
              left: 0
              } : t.offset();
          return n.top -= parseFloat(s.css(e, "marginTop")) || 0, n.left -= parseFloat(s.css(e, "marginLeft")) || 0, r.top += parseFloat(s.css(t[0], "borderTopWidth")) || 0, r.left += parseFloat(s.css(t[0], "borderLeftWidth")) || 0, {
            top: n.top - r.top,
            left: n.left - r.left
          }
        },
        offsetParent: function() {
          return this.map(function() {
            var e = this.offsetParent || n.body;
            while (e && !Tn.test(e.nodeName) && s.css(e, "position") === "static") e = e.offsetParent;
            return e
          })
        }
      }), s.each(["Left", "Top"], function(e, n) {
        var r = "scroll" + n;
        s.fn[r] = function(n) {
          var i, o;
          return n === t ? (i = this[0], i ? (o = Nn(i), o ? "pageXOffset" in o ? o[e ? "pageYOffset" : "pageXOffset"] : s.support.boxModel && o.document.documentElement[r] || o.document.body[r] : i[r]) : null) : this.each(function() {
            o = Nn(this), o ? o.scrollTo(e ? s(o).scrollLeft() : n, e ? n : s(o).scrollTop()) : this[r] = n
          })
        }
      }), s.each(["Height", "Width"], function(e, n) {
        var r = n.toLowerCase();
        s.fn["inner" + n] = function() {
          var e = this[0];
          return e ? e.style ? parseFloat(s.css(e, r, "padding")) : this[r]() : null
        }, s.fn["outer" + n] = function(e) {
          var t = this[0];
          return t ? t.style ? parseFloat(s.css(t, r, e ? "margin" : "border")) : this[r]() : null
        }, s.fn[r] = function(e) {
          var i = this[0];
          if (!i) return e == null ? null : this;
          if (s.isFunction(e)) return this.each(function(t) {
            var n = s(this);
            n[r](e.call(this, t, n[r]()))
          });
          if (s.isWindow(i)) {
            var o = i.document.documentElement["client" + n],
                u = i.document.body;
            return i.document.compatMode === "CSS1Compat" && o || u && u["client" + n] || o
          }
          if (i.nodeType === 9) return Math.max(i.documentElement["client" + n], i.body["scroll" + n], i.documentElement["scroll" + n], i.body["offset" + n], i.documentElement["offset" + n]);
          if (e === t) {
            var a = s.css(i, r),
                f = parseFloat(a);
            return s.isNumeric(f) ? f : a
          }
          return this.css(r, typeof e == "string" ? e : e + "px")
        }
      }), e.jQuery = e.$ = s, typeof define == "function" && define.amd && define.amd.jQuery && define("jquery", [], function() {
        return s
      })
    })(window), n.exports = jQuery
  },
  "pagedown/node-pagedown": function(e, t, n) {
    e.Converter = t("./Markdown.Converter").Converter, e.getSanitizingConverter = t("./Markdown.Sanitizer").getSanitizingConverter
  },
  "pagedown/Markdown.Converter": function(e, t, n) {
    var r;
    typeof e == "object" && typeof t == "function" ? r = e : r = {}, function() {
      function e(e) {
        return e
      }
      function t(e) {
        return !1
      }
      function n() {}
      function i() {}
      n.prototype = {
        chain: function(t, n) {
          var r = this[t];
          if (!r) throw new Error("unknown hook " + t);
          r === e ? this[t] = n : this[t] = function(e) {
            var t = Array.prototype.slice.call(arguments, 0);
            return t[0] = r.apply(null, t), n.apply(null, t)
          }
        },
        set: function(e, t) {
          if (!this[e]) throw new Error("unknown hook " + e);
          this[e] = t
        },
        addNoop: function(t) {
          this[t] = e
        },
        addFalse: function(e) {
          this[e] = t
        }
      }, r.HookCollection = n, i.prototype = {
        set: function(e, t) {
          this["s_" + e] = t
        },
        get: function(e) {
          return this["s_" + e]
        }
      }, r.Converter = function() {
        function u(e) {
          return e = e.replace(/^[ ]{0,3}\[(.+)\]:[ \t]*\n?[ \t]*<?(\S+?)>?(?=\s|$)[ \t]*\n?[ \t]*((\n*)["(](.+?)[")][ \t]*)?(?:\n+)/gm, function(e, n, i, s, o, u) {
            return n = n.toLowerCase(), t.set(n, O(i)), o ? s : (u && r.set(n, u.replace(/"/g, "&quot;")), "")
          }), e
        }
        function a(e) {
          var t = "p|div|h[1-6]|blockquote|pre|table|dl|ol|ul|script|noscript|form|fieldset|iframe|math|ins|del",
              n = "p|div|h[1-6]|blockquote|pre|table|dl|ol|ul|script|noscript|form|fieldset|iframe|math";
          return e = e.replace(/^(<(p|div|h[1-6]|blockquote|pre|table|dl|ol|ul|script|noscript|form|fieldset|iframe|math|ins|del)\b[^\r]*?\n<\/\2>[ \t]*(?=\n+))/gm, f), e = e.replace(/^(<(p|div|h[1-6]|blockquote|pre|table|dl|ol|ul|script|noscript|form|fieldset|iframe|math)\b[^\r]*?.*<\/\2>[ \t]*(?=\n+)\n)/gm, f), e = e.replace(/\n[ ]{0,3}((<(hr)\b([^<>])*?\/?>)[ \t]*(?=\n{2,}))/g, f), e = e.replace(/\n\n[ ]{0,3}(<!(--(?:|(?:[^>-]|-[^>])(?:[^-]|-[^-])*)--)>[ \t]*(?=\n{2,}))/g, f), e = e.replace(/(?:\n\n)([ ]{0,3}(?:<([?%])[^\r]*?\2>)[ \t]*(?=\n{2,}))/g, f), e
        }
        function f(e, t) {
          var n = t;
          return n = n.replace(/^\n+/, ""), n = n.replace(/\n+$/g, ""), n = "\n\n~K" + (s.push(n) - 1) + "K\n\n", n
        }
        function c(t, n) {
          t = e.preBlockGamut(t, l), t = b(t);
          var r = "<hr />\n";
          return t = t.replace(/^[ ]{0,2}([ ]?\*[ ]?){3,}[ \t]*$/gm, r), t = t.replace(/^[ ]{0,2}([ ]?-[ ]?){3,}[ \t]*$/gm, r), t = t.replace(/^[ ]{0,2}([ ]?_[ ]?){3,}[ \t]*$/gm, r), t = w(t), t = x(t), t = L(t), t = e.postBlockGamut(t, l), t = a(t), t = A(t, n), t
        }
        function h(t) {
          return t = e.preSpanGamut(t), t = N(t), t = p(t), t = M(t), t = m(t), t = d(t), t = D(t), t = t.replace(/~P/g, "://"), t = O(t), t = k(t), t = t.replace(/  +\n/g, " <br>\n"), t = e.postSpanGamut(t), t
        }
        function p(e) {
          var t = /(<[a-z\/!$]("[^"]*"|'[^']*'|[^'">])*>|<!(--(?:|(?:[^>-]|-[^>])(?:[^-]|-[^-])*)--)>)/gi;
          return e = e.replace(t, function(e) {
            var t = e.replace(/(.)<\/?code>(?=.)/g, "$1`");
            return t = I(t, e.charAt(1) == "!" ? "\\`*_/" : "\\`*_"), t
          }), e
        }
        function d(e) {
          return e = e.replace(/(\[((?:\[[^\]]*\]|[^\[\]])*)\][ ]?(?:\n[ ]*)?\[(.*?)\])()()()()/g, v), e = e.replace(/(\[((?:\[[^\]]*\]|[^\[\]])*)\]\([ \t]*()<?((?:\([^)]*\)|[^()\s])*?)>?[ \t]*((['"])(.*?)\6[ \t]*)?\))/g, v), e = e.replace(/(\[([^\[\]]+)\])()()()()()/g, v), e
        }
        function v(e, n, i, s, o, u, a, f) {
          f == undefined && (f = "");
          var l = n,
              c = i.replace(/:\/\//g, "~P"),
              h = s.toLowerCase(),
              p = o,
              d = f;
          if (p == "") {
            h == "" && (h = c.toLowerCase().replace(/ ?\n/g, " ")), p = "#" + h;
            if (t.get(h) != undefined) p = t.get(h), r.get(h) != undefined && (d = r.get(h));
            else {
              if (!(l.search(/\(\s*\)$/m) > -1)) return l;
              p = ""
            }
          }
          p = F(p), p = I(p, "*_");
          var v = '<a href="' + p + '"';
          return d != "" && (d = g(d), d = I(d, "*_"), v += ' title="' + d + '"'), v += ">" + c + "</a>", v
        }
        function m(e) {
          return e = e.replace(/(!\[(.*?)\][ ]?(?:\n[ ]*)?\[(.*?)\])()()()()/g, y), e = e.replace(/(!\[(.*?)\]\s?\([ \t]*()<?(\S+?)>?[ \t]*((['"])(.*?)\6[ \t]*)?\))/g, y), e
        }
        function g(e) {
          return e.replace(/>/g, "&gt;").replace(/</g, "&lt;").replace(/"/g, "&quot;")
        }
        function y(e, n, i, s, o, u, a, f) {
          var l = n,
              c = i,
              h = s.toLowerCase(),
              p = o,
              d = f;
          d || (d = "");
          if (p == "") {
            h == "" && (h = c.toLowerCase().replace(/ ?\n/g, " ")), p = "#" + h;
            if (t.get(h) == undefined) return l;
            p = t.get(h), r.get(h) != undefined && (d = r.get(h))
          }
          c = I(g(c), "*_[]()"), p = I(p, "*_");
          var v = '<img src="' + p + '" alt="' + c + '"';
          return d = g(d), d = I(d, "*_"), v += ' title="' + d + '"', v += " />", v
        }
        function b(e) {
          return e = e.replace(/^(.+)[ \t]*\n=+[ \t]*\n+/gm, function(e, t) {
            return "<h1>" + h(t) + "</h1>\n\n"
          }), e = e.replace(/^(.+)[ \t]*\n-+[ \t]*\n+/gm, function(e, t) {
            return "<h2>" + h(t) + "</h2>\n\n"
          }), e = e.replace(/^(\#{1,6})[ \t]*(.+?)[ \t]*\#*\n+/gm, function(e, t, n) {
            var r = t.length;
            return "<h" + r + ">" + h(n) + "</h" + r + ">\n\n"
          }), e
        }
        function w(e) {
          e += "~0";
          var t = /^(([ ]{0,3}([*+-]|\d+[.])[ \t]+)[^\r]+?(~0|\n{2,}(?=\S)(?![ \t]*(?:[*+-]|\d+[.])[ \t]+)))/gm;
          return o ? e = e.replace(t, function(e, t, n) {
            var r = t,
                i = n.search(/[*+-]/g) > -1 ? "ul" : "ol",
                s = S(r, i);
            return s = s.replace(/\s+$/, ""), s = "<" + i + ">" + s + "</" + i + ">\n", s
          }) : (t = /(\n\n|^\n?)(([ ]{0,3}([*+-]|\d+[.])[ \t]+)[^\r]+?(~0|\n{2,}(?=\S)(?![ \t]*(?:[*+-]|\d+[.])[ \t]+)))/g, e = e.replace(t, function(e, t, n, r) {
            var i = t,
                s = n,
                o = r.search(/[*+-]/g) > -1 ? "ul" : "ol",
                u = S(s, o);
            return u = i + "<" + o + ">\n" + u + "</" + o + ">\n", u
          })), e = e.replace(/~0/, ""), e
        }
        function S(e, t) {
          o++, e = e.replace(/\n{2,}$/, "\n"), e += "~0";
          var n = E[t],
              r = new RegExp("(^[ \\t]*)(" + n + ")[ \\t]+([^\\r]+?(\\n+))(?=(~0|\\1(" + n + ")[ \\t]+))", "gm"),
              i = !1;
          return e = e.replace(r, function(e, t, n, r) {
            var s = r,
                o = t,
                u = /\n\n$/.test(s),
                a = u || s.search(/\n{2,}/) > -1;
            return a || i ? s = c(H(s), !0) : (s = w(H(s)), s = s.replace(/\n$/, ""), s = h(s)), i = u, "<li>" + s + "</li>\n"
          }), e = e.replace(/~0/g, ""), o--, e
        }
        function x(e) {
          return e += "~0", e = e.replace(/(?:\n\n|^)((?:(?:[ ]{4}|\t).*\n+)+)(\n*[ ]{0,3}[^ \t\n]|(?=~0))/g, function(e, t, n) {
            var r = t,
                i = n;
            return r = C(H(r)), r = B(r), r = r.replace(/^\n+/g, ""), r = r.replace(/\n+$/g, ""), r = "<pre><code>" + r + "\n</code></pre>", "\n\n" + r + "\n\n" + i
          }), e = e.replace(/~0/, ""), e
        }
        function T(e) {
          return e = e.replace(/(^\n+|\n+$)/g, ""), "\n\n~K" + (s.push(e) - 1) + "K\n\n"
        }
        function N(e) {
          return e = e.replace(/(^|[^\\])(`+)([^\r]*?[^`])\2(?!`)/gm, function(e, t, n, r, i) {
            var s = r;
            return s = s.replace(/^([ \t]*)/g, ""), s = s.replace(/[ \t]*$/g, ""), s = C(s), s = s.replace(/:\/\//g, "~P"), t + "<code>" + s + "</code>"
          }), e
        }
        function C(e) {
          return e = e.replace(/&/g, "&amp;"), e = e.replace(/</g, "&lt;"), e = e.replace(/>/g, "&gt;"), e = I(e, "*_{}[]\\", !1), e
        }
        function k(e) {
          return e = e.replace(/([\W_]|^)(\*\*|__)(?=\S)([^\r]*?\S[\*_]*)\2([\W_]|$)/g, "$1<strong>$3</strong>$4"), e = e.replace(/([\W_]|^)(\*|_)(?=\S)([^\r\*_]*?\S)\2([\W_]|$)/g, "$1<em>$3</em>$4"), e
        }
        function L(e) {
          return e = e.replace(/((^[ \t]*>[ \t]?.+\n(.+\n)*\n*)+)/gm, function(e, t) {
            var n = t;
            return n = n.replace(/^[ \t]*>[ \t]?/gm, "~0"), n = n.replace(/~0/g, ""), n = n.replace(/^[ \t]+$/gm, ""), n = c(n), n = n.replace(/(^|\n)/g, "$1  "), n = n.replace(/(\s*<pre>[^\r]+?<\/pre>)/gm, function(e, t) {
              var n = t;
              return n = n.replace(/^  /mg, "~0"), n = n.replace(/~0/g, ""), n
            }), T("<blockquote>\n" + n + "\n</blockquote>")
          }), e
        }
        function A(e, t) {
          e = e.replace(/^\n+/g, ""), e = e.replace(/\n+$/g, "");
          var n = e.split(/\n{2,}/g),
              r = [],
              i = /~K(\d+)K/,
              o = n.length;
          for (var u = 0; u < o; u++) {
            var a = n[u];
            i.test(a) ? r.push(a) : /\S/.test(a) && (a = h(a), a = a.replace(/^([ \t]*)/g, "<p>"), a += "</p>", r.push(a))
          }
          if (!t) {
            o = r.length;
            for (var u = 0; u < o; u++) {
              var f = !0;
              while (f) f = !1, r[u] = r[u].replace(/~K(\d+)K/g, function(e, t) {
                return f = !0, s[t]
              })
            }
          }
          return r.join("\n\n")
        }
        function O(e) {
          return e = e.replace(/&(?!#?[xX]?(?:[0-9a-fA-F]+|\w+);)/g, "&amp;"), e = e.replace(/<(?![a-z\/?!]|~D)/gi, "&lt;"), e
        }
        function M(e) {
          return e = e.replace(/\\(\\)/g, q), e = e.replace(/\\([`*_{}\[\]()>#+-.!])/g, q), e
        }
        function _(e, t, n, r) {
          if (t) return e;
          if (r.charAt(r.length - 1) !== ")") return "<" + n + r + ">";
          var i = r.match(/[()]/g),
              s = 0;
          for (var o = 0; o < i.length; o++) i[o] === "(" ? s <= 0 ? s = 1 : s++ : s--;
          var u = "";
          if (s < 0) {
            var a = new RegExp("\\){1," + -s + "}$");
            r = r.replace(a, function(e) {
              return u = e, ""
            })
          }
          return "<" + n + r + ">" + u
        }
        function D(t) {
          t = t.replace(/(="|<)?\b(https?|ftp)(:\/\/[-A-Z0-9+&@#\/%?=~_|\[\]\(\)!:,\.;]*[-A-Z0-9+&@#\/%=~_|\[\])])(?=$|\W)/gi, _);
          var n = function(t, n) {
              return '<a href="' + n + '">' + e.plainLinkText(n) + "</a>"
              };
          return t = t.replace(/<((https?|ftp):[^'">\s]+)>/gi, n), t
        }
        function P(e) {
          return e = e.replace(/~E(\d+)E/g, function(e, t) {
            var n = parseInt(t);
            return String.fromCharCode(n)
          }), e
        }
        function H(e) {
          return e = e.replace(/^(\t|[ ]{1,4})/gm, "~0"), e = e.replace(/~0/g, ""), e
        }
        function B(e) {
          if (!/\t/.test(e)) return e;
          var t = ["    ", "   ", "  ", " "],
              n = 0,
              r;
          return e.replace(/[\n\t]/g, function(e, i) {
            return e === "\n" ? (n = i + 1, e) : (r = (i - n) % 4, n = i + 1, t[r])
          })
        }
        function F(e) {
          if (!e) return "";
          var t = e.length;
          return e.replace(j, function(n, r) {
            if (n == "~D") return "%24";
            if (n == ":") if (r == t - 1 || /[0-9\/]/.test(e.charAt(r + 1))) return ":";
            return "%" + n.charCodeAt(0).toString(16)
          })
        }
        function I(e, t, n) {
          var r = "([" + t.replace(/([\[\]\\])/g, "\\$1") + "])";
          n && (r = "\\\\" + r);
          var i = new RegExp(r, "g");
          return e = e.replace(i, q), e
        }
        function q(e, t) {
          var n = t.charCodeAt(0);
          return "~E" + n + "E"
        }
        var e = this.hooks = new n;
        e.addNoop("plainLinkText"), e.addNoop("preConversion"), e.addNoop("postNormalization"), e.addNoop("preBlockGamut"), e.addNoop("postBlockGamut"), e.addNoop("preSpanGamut"), e.addNoop("postSpanGamut"), e.addNoop("postConversion");
        var t, r, s, o;
        this.makeHtml = function(n) {
          if (t) throw new Error("Recursive call to converter.makeHtml");
          return t = new i, r = new i, s = [], o = 0, n = e.preConversion(n), n = n.replace(/~/g, "~T"), n = n.replace(/\$/g, "~D"), n = n.replace(/\r\n/g, "\n"), n = n.replace(/\r/g, "\n"), n = "\n\n" + n + "\n\n", n = B(n), n = n.replace(/^[ \t]+$/mg, ""), n = e.postNormalization(n), n = a(n), n = u(n), n = c(n), n = P(n), n = n.replace(/~D/g, "$$"), n = n.replace(/~T/g, "~"), n = e.postConversion(n), s = r = t = null, n
        };
        var l = function(e) {
            return c(e)
            },
            E = {
            ol: "\\d+[.]",
            ul: "[*+-]"
            },
            j = /(?:["'*()[\]:]|~D)/g
      }
    }()
  },
  "pagedown/Markdown.Sanitizer": function(e, t, n) {
    (function() {
      function i(e) {
        return e.replace(/<[^>]*>?/gi, a)
      }
      function a(e) {
        return e.match(s) || e.match(o) || e.match(u) ? e : ""
      }
      function f(e) {
        if (e == "") return "";
        var t = /<\/?\w+[^>]*(\s|$|>)/g,
            n = e.toLowerCase().match(t),
            r = (n || []).length;
        if (r == 0) return e;
        var i, s, o = "<p><img><br><li><hr>",
            u, a = [],
            f = [],
            l = !1;
        for (var c = 0; c < r; c++) {
          i = n[c].replace(/<\/?(\w+).*/, "$1");
          if (a[c] || o.search("<" + i + ">") > -1) continue;
          s = n[c], u = -1;
          if (!/^<\//.test(s)) for (var h = c + 1; h < r; h++) if (!a[h] && n[h] == "</" + i + ">") {
            u = h;
            break
          }
          u == -1 ? l = f[c] = !0 : a[u] = !0
        }
        if (!l) return e;
        var c = 0;
        return e = e.replace(t, function(e) {
          var t = f[c] ? "" : e;
          return c++, t
        }), e
      }
      var n, r;
      typeof e == "object" && typeof t == "function" ? (n = e, r = t("./Markdown.Converter").Converter) : (n = window.Markdown, r = n.Converter), n.getSanitizingConverter = function() {
        var e = new r;
        return e.hooks.chain("postConversion", i), e.hooks.chain("postConversion", f), e
      };
      var s = /^(<\/?(b|blockquote|code|del|dd|dl|dt|em|h1|h2|h3|i|kbd|li|ol|p|pre|s|sup|sub|strong|strike|ul)>|<(br|hr)\s?\/?>)$/i,
          o = /^(<a\shref="((https?|ftp):\/\/|\/)[-A-Za-z0-9+&@#\/%?=~_|!:,.;\(\)]+"(\stitle="[^"<>]+")?\s?>|<\/a>)$/i,
          u = /^(<img\ssrc="(https?:\/\/|\/)[-A-Za-z0-9+&@#\/%?=~_|!:,.;\(\)]+"(\swidth="\d{1,3}")?(\sheight="\d{1,3}")?(\salt="[^"<>]*")?(\stitle="[^"<>]*")?\s?\/?>)$/i
    })()
  },
  "spine/index": function(e, t, n) {
    n.exports = t("./lib/spine")
  },
  "spine/lib/spine": function(e, t, n) {
    (function() {
      var e, t, r, i, s, o, u, a, f, l, c, h, p = [].slice,
          d = [].indexOf ||
      function(e) {
        for (var t = 0, n = this.length; t < n; t++) if (t in this && this[t] === e) return t;
        return -1
      }, v = {}.hasOwnProperty, m = function(e, t) {
        function r() {
          this.constructor = e
        }
        for (var n in t) v.call(t, n) && (e[n] = t[n]);
        return r.prototype = t.prototype, e.prototype = new r, e.__super__ = t.prototype, e
      }, g = function(e, t) {
        return function() {
          return e.apply(t, arguments)
        }
      };
      r = {
        bind: function(e, t) {
          var n, r, i, s, o;
          r = e.split(" "), n = this.hasOwnProperty("_callbacks") && this._callbacks || (this._callbacks = {});
          for (s = 0, o = r.length; s < o; s++) i = r[s], n[i] || (n[i] = []), n[i].push(t);
          return this
        },
        one: function(e, t) {
          return this.bind(e, function() {
            return this.unbind(e, arguments.callee), t.apply(this, arguments)
          })
        },
        trigger: function() {
          var e, t, n, r, i, s, o;
          e = 1 <= arguments.length ? p.call(arguments, 0) : [], n = e.shift(), r = this.hasOwnProperty("_callbacks") && ((o = this._callbacks) != null ? o[n] : void 0);
          if (!r) return;
          for (i = 0, s = r.length; i < s; i++) {
            t = r[i];
            if (t.apply(this, e) === !1) break
          }
          return !0
        },
        unbind: function(e, t) {
          var n, r, i, s, o, u, a, f, l, c;
          if (!e) return this._callbacks = {}, this;
          r = e.split(" ");
          for (u = 0, f = r.length; u < f; u++) {
            o = r[u], s = (c = this._callbacks) != null ? c[o] : void 0;
            if (!s) continue;
            if (!t) {
              delete this._callbacks[o];
              continue
            }
            for (i = a = 0, l = s.length; a < l; i = ++a) {
              n = s[i];
              if (n !== t) continue;
              s = s.slice(), s.splice(i, 1), this._callbacks[o] = s;
              break
            }
          }
          return this
        }
      }, r.on = r.bind, r.off = r.unbind, i = {
        trace: !0,
        logPrefix: "(App)",
        log: function() {
          var e;
          e = 1 <= arguments.length ? p.call(arguments, 0) : [];
          if (!this.trace) return;
          return this.logPrefix && e.unshift(this.logPrefix), typeof console != "undefined" && console !== null && typeof console.log == "function" && console.log.apply(console, e), this
        }
      }, h = ["included", "extended"], o = function() {
        function e() {
          typeof this.init == "function" && this.init.apply(this, arguments)
        }
        return e.include = function(e) {
          var t, n, r;
          if (!e) throw new Error("include(obj) requires obj");
          for (t in e) n = e[t], d.call(h, t) < 0 && (this.prototype[t] = n);
          return (r = e.included) != null && r.apply(this), this
        }, e.extend = function(e) {
          var t, n, r;
          if (!e) throw new Error("extend(obj) requires obj");
          for (t in e) n = e[t], d.call(h, t) < 0 && (this[t] = n);
          return (r = e.extended) != null && r.apply(this), this
        }, e.proxy = function(e) {
          var t = this;
          return function() {
            return e.apply(t, arguments)
          }
        }, e.prototype.proxy = function(e) {
          var t = this;
          return function() {
            return e.apply(t, arguments)
          }
        }, e
      }(), s = function(t) {
        function n(e) {
          n.__super__.constructor.apply(this, arguments), e && this.load(e), this.cid = this.constructor.uid("c-")
        }
        return m(n, t), n.extend(r), n.records = {}, n.crecords = {}, n.attributes = [], n.configure = function() {
          var e, t;
          return t = arguments[0], e = 2 <= arguments.length ? p.call(arguments, 1) : [], this.className = t, this.records = {}, this.crecords = {}, e.length && (this.attributes = e), this.attributes && (this.attributes = c(this.attributes)), this.attributes || (this.attributes = []), this.unbind(), this
        }, n.toString = function() {
          return "" + this.className + "(" + this.attributes.join(", ") + ")"
        }, n.find = function(e) {
          var t;
          t = this.records[e];
          if (!t && ("" + e).match(/c-\d+/)) return this.findCID(e);
          if (!t) throw new Error('"' + this.className + '" model could not find a record for the ID "' + e + '"');
          return t.clone()
        }, n.findCID = function(e) {
          var t;
          t = this.crecords[e];
          if (!t) throw new Error('"' + this.className + '" model could not find a record for the ID "' + id + '"');
          return t.clone()
        }, n.exists = function(e) {
          try {
            return this.find(e)
          } catch (t) {
            return !1
          }
        }, n.refresh = function(e, t) {
          var n, r, i, s;
          t == null && (t = {}), t.clear && (this.records = {}, this.crecords = {}), r = this.fromJSON(e), f(r) || (r = [r]);
          for (i = 0, s = r.length; i < s; i++) n = r[i], n.id || (n.id = n.cid), this.records[n.id] = n, this.crecords[n.cid] = n;
          return this.trigger("refresh", this.cloneArray(r)), this
        }, n.select = function(e) {
          var t, n, r;
          return r = function() {
            var r, i;
            r = this.records, i = [];
            for (t in r) n = r[t], e(n) && i.push(n);
            return i
          }.call(this), this.cloneArray(r)
        }, n.findByAttribute = function(e, t) {
          var n, r, i;
          i = this.records;
          for (n in i) {
            r = i[n];
            if (r[e] === t) return r.clone()
          }
          return null
        }, n.findAllByAttribute = function(e, t) {
          return this.select(function(n) {
            return n[e] === t
          })
        }, n.each = function(e) {
          var t, n, r, i;
          r = this.records, i = [];
          for (t in r) n = r[t], i.push(e(n.clone()));
          return i
        }, n.all = function() {
          return this.cloneArray(this.recordsValues())
        }, n.first = function() {
          var e;
          return e = this.recordsValues()[0], e != null ? e.clone() : void 0
        }, n.last = function() {
          var e, t;
          return t = this.recordsValues(), e = t[t.length - 1], e != null ? e.clone() : void 0
        }, n.count = function() {
          return this.recordsValues().length
        }, n.deleteAll = function() {
          var e, t, n, r;
          n = this.records, r = [];
          for (e in n) t = n[e], r.push(delete this.records[e]);
          return r
        }, n.destroyAll = function(e) {
          var t, n, r, i;
          r = this.records, i = [];
          for (t in r) n = r[t], i.push(this.records[t].destroy(e));
          return i
        }, n.update = function(e, t, n) {
          return this.find(e).updateAttributes(t, n)
        }, n.create = function(e, t) {
          var n;
          return n = new this(e), n.save(t)
        }, n.destroy = function(e, t) {
          return this.find(e).destroy(t)
        }, n.change = function(e) {
          return typeof e == "function" ? this.bind("change", e) : this.trigger("change", e)
        }, n.fetch = function(e) {
          return typeof e == "function" ? this.bind("fetch", e) : this.trigger("fetch", e)
        }, n.toJSON = function() {
          return this.recordsValues()
        }, n.fromJSON = function(e) {
          var t, n, r, i;
          if (!e) return;
          typeof e == "string" && (e = JSON.parse(e));
          if (f(e)) {
            i = [];
            for (n = 0, r = e.length; n < r; n++) t = e[n], i.push(new this(t));
            return i
          }
          return new this(e)
        }, n.fromForm = function() {
          var e;
          return (e = new this).fromForm.apply(e, arguments)
        }, n.recordsValues = function() {
          var e, t, n, r;
          t = [], r = this.records;
          for (e in r) n = r[e], t.push(n);
          return t
        }, n.cloneArray = function(e) {
          var t, n, r, i;
          i = [];
          for (n = 0, r = e.length; n < r; n++) t = e[n], i.push(t.clone());
          return i
        }, n.idCounter = 0, n.uid = function(e) {
          var t;
          return e == null && (e = ""), t = e + this.idCounter++, this.exists(t) && (t = this.uid(e)), t
        }, n.prototype.isNew = function() {
          return !this.exists()
        }, n.prototype.isValid = function() {
          return !this.validate()
        }, n.prototype.validate = function() {}, n.prototype.load = function(e) {
          var t, n;
          for (t in e) n = e[t], typeof this[t] == "function" ? this[t](n) : this[t] = n;
          return this
        }, n.prototype.attributes = function() {
          var e, t, n, r, i;
          t = {}, i = this.constructor.attributes;
          for (n = 0, r = i.length; n < r; n++) e = i[n], e in this && (typeof this[e] == "function" ? t[e] = this[e]() : t[e] = this[e]);
          return this.id && (t.id = this.id), t
        }, n.prototype.eql = function(e) {
          return !!(e && e.constructor === this.constructor && e.cid === this.cid || e.id && e.id === this.id)
        }, n.prototype.save = function(e) {
          var t, n;
          e == null && (e = {});
          if (e.validate !== !1) {
            t = this.validate();
            if (t) return this.trigger("error", t), !1
          }
          return this.trigger("beforeSave", e), n = this.isNew() ? this.create(e) : this.update(e), this.stripCloneAttrs(), this.trigger("save", e), n
        }, n.prototype.stripCloneAttrs = function() {
          var e, t;
          if (this.hasOwnProperty("cid")) return;
          for (e in this) {
            if (!v.call(this, e)) continue;
            t = this[e], this.constructor.attributes.indexOf(e) > -1 && delete this[e]
          }
          return this
        }, n.prototype.updateAttribute = function(e, t, n) {
          var r;
          return r = {}, r[e] = t, this.updateAttributes(r, n)
        }, n.prototype.updateAttributes = function(e, t) {
          return this.load(e), this.save(t)
        }, n.prototype.changeID = function(e) {
          var t;
          return t = this.constructor.records, t[e] = t[this.id], delete t[this.id], this.id = e, this.save()
        }, n.prototype.destroy = function(e) {
          return e == null && (e = {}), this.trigger("beforeDestroy", e), delete this.constructor.records[this.id], delete this.constructor.crecords[this.cid], this.destroyed = !0, this.trigger("destroy", e), this.trigger("change", "destroy", e), this.unbind(), this
        }, n.prototype.dup = function(e) {
          var t;
          return t = new this.constructor(this.attributes()), e === !1 ? t.cid = this.cid : delete t.id, t
        }, n.prototype.clone = function() {
          return a(this)
        }, n.prototype.reload = function() {
          var e;
          return this.isNew() ? this : (e = this.constructor.find(this.id), this.load(e.attributes()), e)
        }, n.prototype.toJSON = function() {
          return this.attributes()
        }, n.prototype.toString = function() {
          return "<" + this.constructor.className + " (" + JSON.stringify(this) + ")>"
        }, n.prototype.fromForm = function(t) {
          var n, r, i, s, o;
          r = {}, o = e(t).serializeArray();
          for (i = 0, s = o.length; i < s; i++) n = o[i], r[n.name] = n.value;
          return this.load(r)
        }, n.prototype.exists = function() {
          return this.id && this.id in this.constructor.records
        }, n.prototype.update = function(e) {
          var t, n;
          return this.trigger("beforeUpdate", e), n = this.constructor.records, n[this.id].load(this.attributes()), t = n[this.id].clone(), t.trigger("update", e), t.trigger("change", "update", e), t
        }, n.prototype.create = function(e) {
          var t, n;
          return this.trigger("beforeCreate", e), this.id || (this.id = this.cid), n = this.dup(!1), this.constructor.records[this.id] = n, this.constructor.crecords[this.cid] = n, t = n.clone(), t.trigger("create", e), t.trigger("change", "create", e), t
        }, n.prototype.bind = function(e, t) {
          var n, r, i = this;
          return this.constructor.bind(e, n = function(e) {
            if (e && i.eql(e)) return t.apply(i, arguments)
          }), this.constructor.bind("unbind", r = function(t) {
            if (t && i.eql(t)) return i.constructor.unbind(e, n), i.constructor.unbind("unbind", r)
          }), n
        }, n.prototype.one = function(e, t) {
          var n, r = this;
          return n = this.bind(e, function() {
            return r.constructor.unbind(e, n), t.apply(r, arguments)
          })
        }, n.prototype.trigger = function() {
          var e, t;
          return e = 1 <= arguments.length ? p.call(arguments, 0) : [], e.splice(1, 0, this), (t = this.constructor).trigger.apply(t, e)
        }, n.prototype.unbind = function() {
          return this.trigger("unbind")
        }, n
      }(o), t = function(t) {
        function n(t) {
          this.release = g(this.release, this);
          var r, i, s;
          this.options = t, s = this.options;
          for (r in s) i = s[r], this[r] = i;
          this.el || (this.el = document.createElement(this.tag)), this.el = e(this.el), this.$el = this.el, this.className && this.el.addClass(this.className), this.attributes && this.el.attr(this.attributes), this.events || (this.events = this.constructor.events), this.elements || (this.elements = this.constructor.elements), this.events && this.delegateEvents(this.events), this.elements && this.refreshElements(), n.__super__.constructor.apply(this, arguments)
        }
        return m(n, t), n.include(r), n.include(i), n.prototype.eventSplitter = /^(\S+)\s*(.*)$/, n.prototype.tag = "div", n.prototype.release = function() {
          return this.trigger("release"), this.el.remove(), this.unbind()
        }, n.prototype.$ = function(t) {
          return e(t, this.el)
        }, n.prototype.delegateEvents = function(e) {
          var t, n, r, i, s, o, u = this;
          o = [];
          for (n in e) {
            i = e[n];
            if (typeof i == "function") i = function(e) {
              return function() {
                return e.apply(u, arguments), !0
              }
            }(i);
            else {
              if (!this[i]) throw new Error("" + i + " doesn't exist");
              i = function(e) {
                return function() {
                  return u[e].apply(u, arguments), !0
                }
              }(i)
            }
            r = n.match(this.eventSplitter), t = r[1], s = r[2], s === "" ? o.push(this.el.bind(t, i)) : o.push(this.el.delegate(s, t, i))
          }
          return o
        }, n.prototype.refreshElements = function() {
          var e, t, n, r;
          n = this.elements, r = [];
          for (e in n) t = n[e], r.push(this[t] = this.$(e));
          return r
        }, n.prototype.delay = function(e, t) {
          return setTimeout(this.proxy(e), t || 0)
        }, n.prototype.html = function(e) {
          return this.el.html(e.el || e), this.refreshElements(), this.el
        }, n.prototype.append = function() {
          var e, t, n;
          return t = 1 <= arguments.length ? p.call(arguments, 0) : [], t = function() {
            var n, r, i;
            i = [];
            for (n = 0, r = t.length; n < r; n++) e = t[n], i.push(e.el || e);
            return i
          }(), (n = this.el).append.apply(n, t), this.refreshElements(), this.el
        }, n.prototype.appendTo = function(e) {
          return this.el.appendTo(e.el || e), this.refreshElements(), this.el
        }, n.prototype.prepend = function() {
          var e, t, n;
          return t = 1 <= arguments.length ? p.call(arguments, 0) : [], t = function() {
            var n, r, i;
            i = [];
            for (n = 0, r = t.length; n < r; n++) e = t[n], i.push(e.el || e);
            return i
          }(), (n = this.el).prepend.apply(n, t), this.refreshElements(), this.el
        }, n.prototype.replace = function(t) {
          var n, r;
          return r = [this.el, e(t.el || t)], n = r[0], this.el = r[1], n.replaceWith(this.el), this.delegateEvents(this.events), this.refreshElements(), this.el
        }, n
      }(o), e = (typeof window != "undefined" && window !== null ? window.jQuery : void 0) || (typeof window != "undefined" && window !== null ? window.Zepto : void 0) ||
      function(e) {
        return e
      }, a = Object.create ||
      function(e) {
        var t;
        return t = function() {}, t.prototype = e, new t
      }, f = function(e) {
        return Object.prototype.toString.call(e) === "[object Array]"
      }, l = function(e) {
        var t;
        if (!e) return !0;
        for (t in e) return !1;
        return !0
      }, c = function(e) {
        return Array.prototype.slice.call(e, 0)
      }, u = this.Spine = {}, typeof n != "undefined" && n !== null && (n.exports = u), u.version = "1.0.9", u.isArray = f, u.isBlank = l, u.$ = e, u.Events = r, u.Log = i, u.Module = o, u.Controller = t, u.Model = s, o.extend.call(u, r), o.create = o.sub = t.create = t.sub = s.sub = function(e, t) {
        var n;
        return n = function(e) {
          function t() {
            return t.__super__.constructor.apply(this, arguments)
          }
          return m(t, e), t
        }(this), e && n.include(e), t && n.extend(t), typeof n.unbind == "function" && n.unbind(), n
      }, s.setup = function(e, t) {
        var n;
        return t == null && (t = []), n = function(e) {
          function t() {
            return t.__super__.constructor.apply(this, arguments)
          }
          return m(t, e), t
        }(this), n.configure.apply(n, [e].concat(p.call(t))), n
      }, u.Class = o
    }).call(this)
  },
  "spine/lib/local": function(e, t, n) {
    (function() {
      var e;
      e = this.Spine || t("spine"), e.Model.Local = {
        extended: function() {
          return this.change(this.saveLocal), this.fetch(this.loadLocal)
        },
        saveLocal: function() {
          var e;
          return e = JSON.stringify(this), localStorage[this.className] = e
        },
        loadLocal: function() {
          var e;
          return e = localStorage[this.className], this.refresh(e || [], {
            clear: !0
          })
        }
      }, typeof n != "undefined" && n !== null && (n.exports = e.Model.Local)
    }).call(this)
  },
  "spine/lib/ajax": function(e, t, n) {
    (function() {
      var e, r, i, s, o, u, a, f, l, c, h = function(e, t) {
          return function() {
            return e.apply(t, arguments)
          }
          },
          p = {}.hasOwnProperty,
          d = function(e, t) {
          function r() {
            this.constructor = e
          }
          for (var n in t) p.call(t, n) && (e[n] = t[n]);
          return r.prototype = t.prototype, e.prototype = new r, e.__super__ = t.prototype, e
          },
          v = [].slice;
      c = this.Spine || t("spine"), e = c.$, a = c.Model, f = e({}), r = {
        getURL: function(e) {
          return e && (typeof e.url == "function" ? e.url() : void 0) || e.url
        },
        enabled: !0,
        disable: function(e) {
          if (!this.enabled) return e();
          this.enabled = !1;
          try {
            return e()
          } catch (t) {
            throw t
          } finally {
            this.enabled = !0
          }
        },
        queue: function(e) {
          return e ? f.queue(e) : f.queue()
        },
        clearQueue: function() {
          return this.queue([])
        }
      }, i = function() {
        function t() {}
        return t.prototype.defaults = {
          contentType: "application/json",
          dataType: "json",
          processData: !1,
          headers: {
            "X-Requested-With": "XMLHttpRequest"
          }
        }, t.prototype.queue = r.queue, t.prototype.ajax = function(t, n) {
          return e.ajax(this.ajaxSettings(t, n))
        }, t.prototype.ajaxQueue = function(t, n) {
          var i, s, o, u, a;
          return s = null, i = e.Deferred(), o = i.promise(), r.enabled ? (a = this.ajaxSettings(t, n), u = function(t) {
            return s = e.ajax(a).done(i.resolve).fail(i.reject).then(t, t)
          }, o.abort = function(t) {
            var n;
            return s ? s.abort(t) : (n = e.inArray(u, this.queue()), n > -1 && this.queue().splice(n, 1), i.rejectWith(a.context || a, [o, t, ""]), o)
          }, this.queue(u), o) : o
        }, t.prototype.ajaxSettings = function(t, n) {
          return e.extend({}, this.defaults, n, t)
        }, t
      }(), s = function(e) {
        function t(e) {
          this.model = e, this.failResponse = h(this.failResponse, this), this.recordsResponse = h(this.recordsResponse, this)
        }
        return d(t, e), t.prototype.find = function(e, t) {
          var n;
          return n = new this.model({
            id: e
          }), this.ajaxQueue(t, {
            type: "GET",
            url: r.getURL(n)
          }).done(this.recordsResponse).fail(this.failResponse)
        }, t.prototype.all = function(e) {
          return this.ajaxQueue(e, {
            type: "GET",
            url: r.getURL(this.model)
          }).done(this.recordsResponse).fail(this.failResponse)
        }, t.prototype.fetch = function(e, t) {
          var n, r = this;
          return e == null && (e = {}), t == null && (t = {}), (n = e.id) ? (delete e.id, this.find(n, e).done(function(e) {
            return r.model.refresh(e, t)
          })) : this.all(e).done(function(e) {
            return r.model.refresh(e, t)
          })
        }, t.prototype.recordsResponse = function(e, t, n) {
          return this.model.trigger("ajaxSuccess", null, t, n)
        }, t.prototype.failResponse = function(e, t, n) {
          return this.model.trigger("ajaxError", null, e, t, n)
        }, t
      }(i), l = function(e) {
        function t(e) {
          this.record = e, this.failResponse = h(this.failResponse, this), this.recordResponse = h(this.recordResponse, this), this.model = this.record.constructor
        }
        return d(t, e), t.prototype.reload = function(e, t) {
          return this.ajaxQueue(e, {
            type: "GET",
            url: r.getURL(this.record)
          }).done(this.recordResponse(t)).fail(this.failResponse(t))
        }, t.prototype.create = function(e, t) {
          return this.ajaxQueue(e, {
            type: "POST",
            data: JSON.stringify(this.record),
            url: r.getURL(this.model)
          }).done(this.recordResponse(t)).fail(this.failResponse(t))
        }, t.prototype.update = function(e, t) {
          return this.ajaxQueue(e, {
            type: "PUT",
            data: JSON.stringify(this.record),
            url: r.getURL(this.record)
          }).done(this.recordResponse(t)).fail(this.failResponse(t))
        }, t.prototype.destroy = function(e, t) {
          return this.ajaxQueue(e, {
            type: "DELETE",
            url: r.getURL(this.record)
          }).done(this.recordResponse(t)).fail(this.failResponse(t))
        }, t.prototype.recordResponse = function(e) {
          var t = this;
          return e == null && (e = {}), function(n, i, s) {
            var o, u;
            return c.isBlank(n) || t.record.destroyed ? n = !1 : n = t.model.fromJSON(n), r.disable(function() {
              if (n) return n.id && t.record.id !== n.id && t.record.changeID(n.id), t.record.updateAttributes(n.attributes())
            }), t.record.trigger("ajaxSuccess", n, i, s), (o = e.success) != null && o.apply(t.record), (u = e.done) != null ? u.apply(t.record) : void 0
          }
        }, t.prototype.failResponse = function(e) {
          var t = this;
          return e == null && (e = {}), function(n, r, i) {
            var s, o;
            return t.record.trigger("ajaxError", n, r, i), (s = e.error) != null && s.apply(t.record), (o = e.fail) != null ? o.apply(t.record) : void 0
          }
        }, t
      }(i), a.host = "", u = {
        ajax: function() {
          return new l(this)
        },
        url: function() {
          var e, t;
          return e = 1 <= arguments.length ? v.call(arguments, 0) : [], t = r.getURL(this.constructor), t.charAt(t.length - 1) !== "/" && (t += "/"), t += encodeURIComponent(this.id), e.unshift(t), e.join("/")
        }
      }, o = {
        ajax: function() {
          return new s(this)
        },
        url: function() {
          var e;
          return e = 1 <= arguments.length ? v.call(arguments, 0) : [], e.unshift(this.className.toLowerCase() + "s"), e.unshift(a.host), e.join("/")
        }
      }, a.Ajax = {
        extended: function() {
          return this.fetch(this.ajaxFetch), this.change(this.ajaxChange), this.extend(o), this.include(u)
        },
        ajaxFetch: function() {
          var e;
          return (e = this.ajax()).fetch.apply(e, arguments)
        },
        ajaxChange: function(e, t, n) {
          n == null && (n = {});
          if (n.ajax === !1) return;
          return e.ajax()[t](n.ajax, n)
        }
      }, a.Ajax.Methods = {
        extended: function() {
          return this.extend(o), this.include(u)
        }
      }, r.defaults = i.prototype.defaults, c.Ajax = r, typeof n != "undefined" && n !== null && (n.exports = r)
    }).call(this)
  },
  "spine/lib/route": function(e, t, n) {
    (function() {
      var e, r, i, s, o, u, a = {}.hasOwnProperty,
          f = function(e, t) {
          function r() {
            this.constructor = e
          }
          for (var n in t) a.call(t, n) && (e[n] = t[n]);
          return r.prototype = t.prototype, e.prototype = new r, e.__super__ = t.prototype, e
          },
          l = [].slice;
      r = this.Spine || t("spine"), e = r.$, s = /^#*/, o = /:([\w\d]+)/g, u = /\*([\w\d]+)/g, i = /[-[\]{}()+?.,\\^$|#\s]/g, r.Route = function(t) {
        function a(e, t) {
          var n;
          this.path = e, this.callback = t, this.names = [];
          if (typeof e == "string") {
            o.lastIndex = 0;
            while ((n = o.exec(e)) !== null) this.names.push(n[1]);
            u.lastIndex = 0;
            while ((n = u.exec(e)) !== null) this.names.push(n[1]);
            e = e.replace(i, "\\$&").replace(o, "([^/]*)").replace(u, "(.*?)"), this.route = new RegExp("^" + e + "$")
          } else this.route = e
        }
        var n;
        return f(a, t), a.extend(r.Events), a.historySupport = ((n = window.history) != null ? n.pushState : void 0) != null, a.routes = [], a.options = {
          trigger: !0,
          history: !1,
          shim: !1,
          replace: !1
        }, a.add = function(e, t) {
          var n, r, i;
          if (typeof e != "object" || e instanceof RegExp) return this.routes.push(new this(e, t));
          i = [];
          for (n in e) r = e[n], i.push(this.add(n, r));
          return i
        }, a.setup = function(t) {
          t == null && (t = {}), this.options = e.extend({}, this.options, t), this.options.history && (this.history = this.historySupport && this.options.history);
          if (this.options.shim) return;
          return this.history ? e(window).bind("popstate", this.change) : e(window).bind("hashchange", this.change), this.change()
        }, a.unbind = function() {
          if (this.options.shim) return;
          return this.history ? e(window).unbind("popstate", this.change) : e(window).unbind("hashchange", this.change)
        }, a.navigate = function() {
          var t, n, r, i;
          t = 1 <= arguments.length ? l.call(arguments, 0) : [], r = {}, n = t[t.length - 1], typeof n == "object" ? r = t.pop() : typeof n == "boolean" && (r.trigger = t.pop()), r = e.extend({}, this.options, r), i = t.join("/");
          if (this.path === i) return;
          this.path = i, this.trigger("navigate", this.path), r.trigger && this.matchRoute(this.path, r);
          if (r.shim) return;
          return this.history && r.replace ? history.replaceState({}, document.title, this.path) : this.history ? history.pushState({}, document.title, this.path) : window.location.hash = this.path
        }, a.getPath = function() {
          var e;
          return this.history ? (e = window.location.pathname, e.substr(0, 1) !== "/" && (e = "/" + e)) : (e = window.location.hash, e = e.replace(s, "")), e
        }, a.getHost = function() {
          return "" + window.location.protocol + "//" + window.location.host
        }, a.change = function() {
          var e;
          e = this.getPath();
          if (e === this.path) return;
          return this.path = e, this.matchRoute(this.path)
        }, a.matchRoute = function(e, t) {
          var n, r, i, s;
          s = this.routes;
          for (r = 0, i = s.length; r < i; r++) {
            n = s[r];
            if (!n.match(e, t)) continue;
            return this.trigger("change", n, e), n
          }
        }, a.prototype.match = function(e, t) {
          var n, r, i, s, o, u;
          t == null && (t = {}), r = this.route.exec(e);
          if (!r) return !1;
          t.match = r, s = r.slice(1);
          if (this.names.length) for (n = o = 0, u = s.length; o < u; n = ++o) i = s[n], t[this.names[n]] = i;
          return this.callback.call(null, t) !== !1
        }, a
      }(r.Module), r.Route.change = r.Route.proxy(r.Route.change), r.Controller.include({
        route: function(e, t) {
          return r.Route.add(e, this.proxy(t))
        },
        routes: function(e) {
          var t, n, r;
          r = [];
          for (t in e) n = e[t], r.push(this.route(t, n));
          return r
        },
        navigate: function() {
          return r.Route.navigate.apply(r.Route, arguments)
        }
      }), typeof n != "undefined" && n !== null && (n.exports = r.Route)
    }).call(this)
  },
  "spine/lib/manager": function(e, t, n) {
    (function() {
      var e, r, i = {}.hasOwnProperty,
          s = function(e, t) {
          function r() {
            this.constructor = e
          }
          for (var n in t) i.call(t, n) && (e[n] = t[n]);
          return r.prototype = t.prototype, e.prototype = new r, e.__super__ = t.prototype, e
          },
          o = [].slice;
      r = this.Spine || t("spine"), e = r.$, r.Manager = function(e) {
        function t() {
          this.controllers = [], this.bind("change", this.change), this.add.apply(this, arguments)
        }
        return s(t, e), t.include(r.Events), t.prototype.add = function() {
          var e, t, n, r, i;
          t = 1 <= arguments.length ? o.call(arguments, 0) : [], i = [];
          for (n = 0, r = t.length; n < r; n++) e = t[n], i.push(this.addOne(e));
          return i
        }, t.prototype.addOne = function(e) {
          var t = this;
          return e.bind("active", function() {
            var n;
            return n = 1 <= arguments.length ? o.call(arguments, 0) : [], t.trigger.apply(t, ["change", e].concat(o.call(n)))
          }), e.bind("release", function() {
            return t.controllers.splice(t.controllers.indexOf(e), 1)
          }), this.controllers.push(e)
        }, t.prototype.deactivate = function() {
          return this.trigger.apply(this, ["change", !1].concat(o.call(arguments)))
        }, t.prototype.change = function() {
          var e, t, n, r, i, s, u;
          n = arguments[0], e = 2 <= arguments.length ? o.call(arguments, 1) : [], s = this.controllers, u = [];
          for (r = 0, i = s.length; r < i; r++) t = s[r], t === n ? u.push(t.activate.apply(t, e)) : u.push(t.deactivate.apply(t, e));
          return u
        }, t
      }(r.Module), r.Controller.include({
        active: function() {
          var e;
          return e = 1 <= arguments.length ? o.call(arguments, 0) : [], typeof e[0] == "function" ? this.bind("active", e[0]) : (e.unshift("active"), this.trigger.apply(this, e)), this
        },
        isActive: function() {
          return this.el.hasClass("active")
        },
        activate: function() {
          return this.el.addClass("active"), this
        },
        deactivate: function() {
          return this.el.removeClass("active"), this
        }
      }), r.Stack = function(e) {
        function t() {
          var e, n, i, s, o, u = this;
          t.__super__.constructor.apply(this, arguments), this.manager = new r.Manager, s = this.controllers;
          for (e in s) {
            n = s[e];
            if (this[e] != null) throw Error("'@" + e + "' already assigned - choose a different name");
            this[e] = new n({
              stack: this
            }), this.add(this[e])
          }
          o = this.routes, i = function(e, t) {
            var n;
            return typeof t == "function" && (n = t), n || (n = function() {
              var e;
              return (e = u[t]).active.apply(e, arguments)
            }), u.route(e, n)
          };
          for (e in o) n = o[e], i(e, n);
          this["default"] && this[this["default"]].active()
        }
        return s(t, e), t.prototype.controllers = {}, t.prototype.routes = {}, t.prototype.className = "spine stack", t.prototype.add = function(e) {
          return this.manager.add(e), this.append(e)
        }, t
      }(r.Controller), typeof n != "undefined" && n !== null && (n.exports = r.Manager), typeof n != "undefined" && n !== null && (n.exports.Stack = r.Stack)
    }).call(this)
  },
  "spine/lib/list": function(e, t, n) {
    (function() {
      var e, r, i = function(e, t) {
          return function() {
            return e.apply(t, arguments)
          }
          },
          s = {}.hasOwnProperty,
          o = function(e, t) {
          function r() {
            this.constructor = e
          }
          for (var n in t) s.call(t, n) && (e[n] = t[n]);
          return r.prototype = t.prototype, e.prototype = new r, e.__super__ = t.prototype, e
          };
      r = this.Spine || t("spine"), e = r.$, r.List = function(t) {
        function n() {
          this.change = i(this.change, this), n.__super__.constructor.apply(this, arguments), this.bind("change", this.change)
        }
        return o(n, t), n.prototype.events = {
          "click .item": "click"
        }, n.prototype.selectFirst = !1, n.prototype.template = function() {
          throw "Override template"
        }, n.prototype.change = function(t) {
          this.current = t;
          if (!this.current) {
            this.children().removeClass("active");
            return
          }
          return this.children().removeClass("active"), e(this.children().get(this.items.indexOf(this.current))).addClass("active")
        }, n.prototype.render = function(e) {
          e && (this.items = e), this.html(this.template(this.items)), this.change(this.current);
          if (this.selectFirst && !this.children(".active").length) return this.children(":first").click()
        }, n.prototype.children = function(e) {
          return this.el.children(e)
        }, n.prototype.click = function(t) {
          var n;
          return n = this.items[e(t.currentTarget).index()], this.trigger("change", n), !0
        }, n
      }(r.Controller), typeof n != "undefined" && n !== null && (n.exports = r.List)
    }).call(this)
  },
  "controllers/pages": function(e, t, n) {
    (function() {
      var e, r, i, s, o, u, a = {}.hasOwnProperty,
          f = function(e, t) {
          function r() {
            this.constructor = e
          }
          for (var n in t) a.call(t, n) && (e[n] = t[n]);
          return r.prototype = t.prototype, e.prototype = new r, e.__super__ = t.prototype, e
          };
      u = t("spine"), r = t("models/page"), e = u.$, s = t("controllers/pages_display"), o = t("controllers/pages_index"), i = function(e) {
        function t() {
          t.__super__.constructor.apply(this, arguments), this.index = new o, this.display = new s, this.routes({
            "/pages/:id/edit": function(e) {
              return this.index.active(e), this.display.edit.active(e)
            },
            "/pages/:id": function(e) {
              return this.index.active(e), this.display.display.active(e)
            }
          }), this.append(this.index, this.display), r.fetch()
        }
        return f(t, e), t.prototype.className = "pages", t
      }(u.Controller), n.exports = i
    }).call(this)
  },
  "controllers/pages_display": function(e, t, n) {
    (function() {
      var e, r, i, s, o, u, a, f = function(e, t) {
          return function() {
            return e.apply(t, arguments)
          }
          },
          l = {}.hasOwnProperty,
          c = function(e, t) {
          function r() {
            this.constructor = e
          }
          for (var n in t) l.call(t, n) && (e[n] = t[n]);
          return r.prototype = t.prototype, e.prototype = new r, e.__super__ = t.prototype, e
          };
      a = t("spine"), o = t("models/page"), s = t("pagedown/node-pagedown"), e = a.$, r = function(e) {
        function n() {
          this.change = f(this.change, this), n.__super__.constructor.apply(this, arguments), this.markdown = new s.Converter, this.active(this.change)
        }
        return c(n, e), n.prototype.className = "show", n.prototype.events = {
          "click .edit": "edit"
        }, n.prototype.render = function() {
          return this.html(t("views/display")(this))
        }, n.prototype.change = function(e) {
          return this.item = o.find(e.id), this.render()
        }, n.prototype.edit = function() {
          return this.navigate("/pages", this.item.id, "edit")
        }, n
      }(a.Controller), i = function(e) {
        function n() {
          this.submit = f(this.submit, this), this.change = f(this.change, this), n.__super__.constructor.apply(this, arguments), this.active(this.change)
        }
        return c(n, e), n.prototype.className = "edit", n.prototype.events = {
          "submit form": "submit",
          "click .save": "submit",
          "click .delete": "delete"
        }, n.prototype.elements = {
          form: "form"
        }, n.prototype.render = function() {
          return this.html(t("views/form")(this.item))
        }, n.prototype.change = function(e) {
          return this.item = o.find(e.id), this.render()
        }, n.prototype.submit = function(e) {
          return e.preventDefault(), this.item.fromForm(this.form).save(), this.navigate("/pages", this.item.id)
        }, n.prototype["delete"] = function() {
          if (confirm("Are you sure?") && confim("This is forever. REALLY??")) return this.item.destroy()
        }, n
      }(a.Controller), u = function(e) {
        function t() {
          return t.__super__.constructor.apply(this, arguments)
        }
        return c(t, e), t.prototype.controllers = {
          display: r,
          edit: i
        }, t
      }(a.Stack), n.exports = u
    }).call(this)
  },
  "controllers/pages_index": function(e, t, n) {
    (function() {
      var e, r, i, s, o, u = function(e, t) {
          return function() {
            return e.apply(t, arguments)
          }
          },
          a = {}.hasOwnProperty,
          f = function(e, t) {
          function r() {
            this.constructor = e
          }
          for (var n in t) a.call(t, n) && (e[n] = t[n]);
          return r.prototype = t.prototype, e.prototype = new r, e.__super__ = t.prototype, e
          };
      o = t("spine"), i = t("models/page"), r = t("spine/lib/list"), e = o.$, s = function(e) {
        function n() {
          this.change = u(this.change, this), this.render = u(this.render, this), n.__super__.constructor.apply(this, arguments), this.html(t("views/index")()), this.list = new r({
            el: this.items,
            template: t("views/item"),
            selectFirst: !0
          }), this.list.bind("change", this.change), this.active(function(e) {
            return this.list.change(i.find(e.id))
          }), i.bind("refresh change", this.render)
        }
        return f(n, e), n.prototype.className = "index", n.prototype.elements = {
          ".items": "items",
          "input[type=search]": "search"
        }, n.prototype.events = {
          "keyup input[type=search]": "filter",
          "click footer button": "create"
        }, n.prototype.filter = function() {
          return this.query = this.search.val(), this.render()
        }, n.prototype.render = function() {
          var e;
          return e = i.filter(this.query), this.list.render(e)
        }, n.prototype.change = function(e) {
          return this.navigate("/pages", e.id)
        }, n.prototype.create = function() {
          var e;
          return e = i.create(), this.navigate("/pages", e.id, "edit")
        }, n
      }(o.Controller), n.exports = s
    }).call(this)
  },
  index: function(e, t, n) {
    (function() {
      var e, r, i, s = {}.hasOwnProperty,
          o = function(e, t) {
          function r() {
            this.constructor = e
          }
          for (var n in t) s.call(t, n) && (e[n] = t[n]);
          return r.prototype = t.prototype, e.prototype = new r, e.__super__ = t.prototype, e
          };
      t("lib/setup"), i = t("spine"), r = t("controllers/pages"), e = function(e) {
        function t() {
          t.__super__.constructor.apply(this, arguments), this.pages = new r, this.append(this.pages), i.Route.setup()
        }
        return o(t, e), t
      }(i.Controller), n.exports = e
    }).call(this)
  },
  "lib/setup": function(e, t, n) {
    (function() {
      t("json2ify"), t("es5-shimify"), t("jqueryify"), t("spine"), t("spine/lib/local"), t("spine/lib/ajax"), t("spine/lib/manager"), t("spine/lib/route")
    }).call(this)
  },
  "models/page": function(e, t, n) {
    (function() {
      var e, r, i = {}.hasOwnProperty,
          s = function(e, t) {
          function r() {
            this.constructor = e
          }
          for (var n in t) i.call(t, n) && (e[n] = t[n]);
          return r.prototype = t.prototype, e.prototype = new r, e.__super__ = t.prototype, e
          };
      r = t("spine"), e = function(e) {
        function t() {
          return t.__super__.constructor.apply(this, arguments)
        }
        return s(t, e), t.configure("Page", "path", "title", "content", "created", "updated", "currentAuthor"), t.extend(t.Local), t.filter = function(e) {
          return e ? (e = e.toLowerCase(), this.select(function(t) {
            var n, r;
            return ((n = t.title) != null ? n.toLowerCase().indexOf(e) : void 0) !== -1 || ((r = t.path) != null ? r.toLowerCase().indexOf(e) : void 0) !== -1
          })) : this.all()
        }, t
      }(r.Model), n.exports = e
    }).call(this)
  },
  "views/display": function(e, t, n) {
    n.exports = function(e) {
      e || (e = {});
      var t = [],
          n = function(e) {
          var n = t,
              r;
          return t = [], e.call(this), r = t.join(""), t = n, i(r)
          },
          r = function(e) {
          return e && e.ecoSafe ? e : typeof e != "undefined" && e != null ? o(e) : ""
          },
          i, s = e.safe,
          o = e.escape;
      return i = e.safe = function(e) {
        if (e && e.ecoSafe) return e;
        if (typeof e == "undefined" || e == null) e = "";
        var t = new String(e);
        return t.ecoSafe = !0, t
      }, o || (o = e.escape = function(e) {
        return ("" + e).replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/"/g, "&quot;")
      }), function() {
        (function() {
          t.push('<header>\n  <a class="edit">Edit</a>\n</header>\n\n<div class="content">\n  <p>'), t.push(r(this.item.title)), t.push("</p>\n<!--  <p>"), t.push(r(this.item.created)), t.push("</p>\n  <p>"), t.push(r(this.item.updated)), t.push("</p>\n  <p>"), t.push(r(this.item.currentAuthor)), t.push("</p> -->\n  <br/>\n  <br/>\n  "), this.item.content && t.push(this.markdown.makeHtml(this.item.content)), t.push("\n</div>")
        }).call(this)
      }.call(e), e.safe = s, e.escape = o, t.join("")
    }
  },
  "views/form": function(e, t, n) {
    n.exports = function(e) {
      e || (e = {});
      var t = [],
          n = function(e) {
          var n = t,
              r;
          return t = [], e.call(this), r = t.join(""), t = n, i(r)
          },
          r = function(e) {
          return e && e.ecoSafe ? e : typeof e != "undefined" && e != null ? o(e) : ""
          },
          i, s = e.safe,
          o = e.escape;
      return i = e.safe = function(e) {
        if (e && e.ecoSafe) return e;
        if (typeof e == "undefined" || e == null) e = "";
        var t = new String(e);
        return t.ecoSafe = !0, t
      }, o || (o = e.escape = function(e) {
        return ("" + e).replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/"/g, "&quot;")
      }), function() {
        (function() {
          t.push('<header>\n  <a class="save">Save</a>\n  <a class="delete">Delete</a>\n</header>\n\n<div class="content">\n  <form>\n    <label>\n      <span>Title</span>\n      <input type="text" name="title" value="'), t.push(r(this.title)), t.push('">\n    </label>\n\n    <label>\n      <span>Author</span>\n      <input type="text" name="currentAuthor" value="'), t.push(r(this.currentAuthor)), t.push('">\n    </label>  \n\n    <label>\n      <span>Path</span>\n      <input type="text" name="path" value="'), t.push(r(this.path)), t.push('">\n    </label>  \n    \n    <textarea name="content">'), t.push(r(this.content)), t.push("</textarea>\n\n    <button>Save</button>\n  </form>\n</div>\n")
        }).call(this)
      }.call(e), e.safe = s, e.escape = o, t.join("")
    }
  },
  "views/index": function(e, t, n) {
    n.exports = function(e) {
      e || (e = {});
      var t = [],
          n = function(e) {
          var n = t,
              r;
          return t = [], e.call(this), r = t.join(""), t = n, i(r)
          },
          r = function(e) {
          return e && e.ecoSafe ? e : typeof e != "undefined" && e != null ? o(e) : ""
          },
          i, s = e.safe,
          o = e.escape;
      return i = e.safe = function(e) {
        if (e && e.ecoSafe) return e;
        if (typeof e == "undefined" || e == null) e = "";
        var t = new String(e);
        return t.ecoSafe = !0, t
      }, o || (o = e.escape = function(e) {
        return ("" + e).replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/"/g, "&quot;")
      }), function() {
        (function() {
          t.push('<header>\n  <input type="search" placeholder="search" results="0" incremental="true" autofocus>\n</header>\n\n<div class="items"></div>\n\n<footer>\n  <button>New Contact</button>\n</footer>\n')
        }).call(this)
      }.call(e), e.safe = s, e.escape = o, t.join("")
    }
  },
  "views/item": function(e, t, n) {
    n.exports = function(e, t) {
      var n = jQuery,
          r = n();
      e = n.makeArray(e), t = t || {};
      for (var i = 0; i < e.length; i++) {
        var s = n.extend({}, e[i], t, {
          index: i
        }),
            o = n(function(e) {
            e || (e = {});
            var t = [],
                n = function(e) {
                var n = t,
                    r;
                return t = [], e.call(this), r = t.join(""), t = n, i(r)
                },
                r = function(e) {
                return e && e.ecoSafe ? e : typeof e != "undefined" && e != null ? o(e) : ""
                },
                i, s = e.safe,
                o = e.escape;
            return i = e.safe = function(e) {
              if (e && e.ecoSafe) return e;
              if (typeof e == "undefined" || e == null) e = "";
              var t = new String(e);
              return t.ecoSafe = !0, t
            }, o || (o = e.escape = function(e) {
              return ("" + e).replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/"/g, "&quot;")
            }), function() {
              (function() {
                t.push('<div class="item">\n  '), this.title && t.push(r(this.title)), t.push("\n</div>\n")
              }).call(this)
            }.call(e), e.safe = s, e.escape = o, t.join("")
          }(s));
        o.data("item", s), n.merge(r, o)
      }
      return r
    }
  }
})
