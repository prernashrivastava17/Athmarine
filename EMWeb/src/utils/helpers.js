export const numberValidation = (num) => {
  if (num) {
    return /^(?:[0-9]●?){5,12}[0-9]$/g.test(num);
  }
};

export const emailValidation = (email) => {
  // const re =
  //   /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

  let re =
    /^(?=[^@]*[A-Za-z])([a-zA-Z0-9])(([a-zA-Z0-9])*([\.-_-+])?([a-zA-Z0-9]))*@(([a-zA-Z0-9\-])+(\.))+([a-zA-Z]{2,3})+$/i;
  return re.test(String(email).toLowerCase());
};

export const passwordValidation = (password) => {
  return /(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!"#$%&'()*+,-./:;<=>?@[\]^_`{|}~]).{8,}/.test(
    password
  );
  //   return /(?=.*[_!@#$%^&*-_])(?=.*\d)(?!.*[.\n])(?=.*[a-z])(?=.*[A-Z])^.{8,}$/.test(password);
};

export const truncate = (text, textLimit) => {
  return text.slice(0, 5).concat("...");
};

export const numberWithCommas = (x) => {
  x = x.toString();
  var pattern = /(-?\d+)(\d{3})/;
  while (pattern.test(x)) x = x.replace(pattern, "$1,$2");
  return x;
};
