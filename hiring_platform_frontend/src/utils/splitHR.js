export const splitHR = (data) => {
  const techHRData = data.filter((item) => item.roundType === "Technical");
  const personalHRData = data.filter((item) => item.roundType !== "Technical");
  return {
    techHRData,
    personalHRData,
  };
};

export const splitEmployeeList = (data) => {
  const request_sent = data.filter((item) => item.status === "REQUEST_SENT");
  const accepted = data.filter((item) => item.status === "ACCEPTED");
  const rejected = data.filter((item) => (item.status = "REJECTED"));
  const not_selected = data.filter((item) => (item.status = "NOT_SELECTED"));
  const selected = data.filter((item) => (item.status = "SELECTED"));
  return {
    request_sent,
    accepted,
    rejected,
    not_selected,
    selected,
  };
};

export const camelToSpacePascal = (camelCase) => {
  return camelCase.replace(/([A-Z])/g, " $1").replace(/^./, function (str) {
    return str.toUpperCase();
  });
};
export const toPascalCase = (text, trimSpace = false) =>
  text
    .split(" ")
    .map((t) => t[0].toUpperCase() + t.slice(1).toLowerCase())
    .join(trimSpace ? "" : " ");
