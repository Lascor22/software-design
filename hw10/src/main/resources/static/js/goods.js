const element = function (id) {
    return document.getElementById(id);
}

window.onload = function () {
    if (element("signup")) {
        element("signup").onclick = function (e) {
            element("signup").setAttribute("disabled", "disabled");

            const login = element("login").value;
            const currency = element("currency").value;
            if (login && login.length > 0 && currency) {
                const request = new XMLHttpRequest();

                request.open("POST", "/register-user", true);
                request.setRequestHeader("Content-Type", "application/json");
                request.send(JSON.stringify({
                    "login": login, "currency": currency
                }));

                request.onreadystatechange = function (e) {
                    if (request.readyState === 4) {
                        element("signup").removeAttribute("disabled");
                        const response = request.responseText;
                        console.log(response);

                        if (response === "registered") {
                            location.reload();
                        }
                    }
                };
            }
        }
    }

    if (element("filter")) {
        element("filter").onclick = function (e) {
            element("filter").setAttribute("disabled", "disabled");

            const identifier = element("userIdentifier").value;
            const sorting = element("sorting").value;
            const currency = element("currency").value;
            const withDescription = element("showWithoutDesc").checked;
            const withIcon = element("showWithoutIcon").checked;
            const shape = element("shape").value;
            const color = element("color").value;

            const request = new XMLHttpRequest();

            request.open("POST", "/update-user", true);
            request.setRequestHeader("Content-Type", "application/json");
            request.send(JSON.stringify({
                "identifier": identifier, "currency": currency,
                "sorting": sorting, "withDescription": withDescription,
                "withIcon": withIcon, "shape": shape, "color": color
            }));

            request.onreadystatechange = function (e) {
                if (request.readyState === 4) {
                    element("filter").removeAttribute("disabled");
                    const response = request.responseText;
                    console.log(response);

                    if (response === "updated") {
                        location.reload();
                    }
                }
            };
        }
    }
}