export function JsonStringifyReplacer(key, value) {
  if (value === null) {
    return undefined;
  }
  if (value === ""){
    return undefined;
  }
  return value;
}