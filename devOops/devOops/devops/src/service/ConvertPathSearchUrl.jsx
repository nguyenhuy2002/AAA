const convertPathSearchUrl = (property_value) => {
  const url = new URL(window.location.href);
  const searchParams = url.searchParams;
  if (Array.isArray(property_value))
    for (const propertyValue of property_value) {
      const property = propertyValue.property;
      let value =
        property === "id" ? parseInt(propertyValue.value) : propertyValue.value;
      //   if (Array.isArray(Array.from(value))) if (value.length === 0) value = "";
      if (
        value === "" ||
        !value ||
        (property === "id" && (isNaN(value) || value < 1))
      ) {
        if (searchParams.has(property)) {
          searchParams.delete(property);
        }
      } else {
        if (searchParams.has(property)) {
          searchParams.set(property, value);
        } else {
          searchParams.append(property, value);
        }
      }
    }
  const sP = searchParams.toString();
  return `${url.pathname}${sP != "" ? `?${sP}` : ""}`;
};

export default convertPathSearchUrl;
