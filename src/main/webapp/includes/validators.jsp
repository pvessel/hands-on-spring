emptyValidator = function(value, callback) {
  if (!value || 0 === value.length) {
    callback(false);
  } else {
    callback(true);
  }
};

emailValidator = function (value, callback) {
    if (/.+@.+/.test(value)) {
      callback(true);
    }
    else {
      callback(false);
    }
};

