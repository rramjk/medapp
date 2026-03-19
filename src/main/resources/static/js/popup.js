document.getElementById("open-popup").addEventListener("click", function () {
    document.getElementById("popup").classList.remove("hidden");
});

document.getElementById("close-popup").addEventListener("click", function () {
    document.getElementById("popup").classList.add("hidden");
});

document.getElementById("doctorForm").addEventListener("submit", function (e) {
    const policyAccepted = document.getElementById("policyCheckbox").checked;
    if (!policyAccepted) {
        alert("Вы должны согласиться с политикой конфиденциальности.");
        e.preventDefault();
    }
});
