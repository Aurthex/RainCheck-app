// JavaScript function to change the saved icon and href based on the page title
function updateSavedIcon() {
    var title = document.title; // Get the current page title

    var savedIcon = document.getElementById("icon-image");
    var savedLink = document.getElementById("icon-link");

    // Set the image source and href based on the title
    if (title === "RainCheck") {
        savedIcon.src = "https://i.ibb.co/rHXmBFs/list-icon.png";
        savedLink.href = "/activities";
    } else if (title === "RainCheck Activities") {
        savedIcon.src = "https://i.ibb.co/Qb9zzTY/back-arrow.png";
        savedLink.href = "/";
    } else {
        // Default case if title doesn't match expected values
        savedIcon.src = "https://i.ibb.co/rHXmBFs/list-icon.png";
        savedLink.href = "/";
    }
}

// Call the function once when the page loads
window.onload = function() {
    updateSavedIcon();
};