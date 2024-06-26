// Updates activity details based on selected option in the dropdown
function updateActivityDetails() {
    const selectElement = document.getElementById("activity");
    const selectedOption = selectElement.options[selectElement.selectedIndex];
    const activityData = selectedOption.getAttribute('data-activity');

    if (activityData) {
        // Parse the JSON data associated with the selected activity
        const selectedActivity = JSON.parse(activityData);

        // Update DOM elements with activity details
        document.getElementById("activityId").value = selectedActivity.id;
        document.getElementById("activityDescription").innerText = selectedActivity.description;
        document.getElementById("activityTemp").innerText = selectedActivity.temperature;
        document.getElementById("activityWindSpeed").innerText = selectedActivity.windSpeed;
        document.getElementById("activityScore").innerText = selectedActivity.score;
        document.getElementById("activityScoreMessage").innerText = selectedActivity.scoreMessage;

        // Display conditions
        const conditions = selectedActivity.conditions.map(cond => cond.name).join(", ");
        document.getElementById("activityCond").innerText = conditions;

    } else {
        // Reset DOM elements if no activity is selected
        document.getElementById("activityId").value = '';
        document.getElementById("activityDescription").innerText = '';
        document.getElementById("activityTemp").innerText = '';
        document.getElementById("activityWindSpeed").innerText = '';
        document.getElementById("activityScore").innerText = '';
        document.getElementById("activityScoreMessage").innerText = '';
        document.getElementById("activityCond").innerText = '';
    }
}

// Checks if string 'a' contains string 'b' and updates resultElement with the message
function checkStringContainment(a, b, resultElementId) {
    const resultElement = document.getElementById(resultElementId);
    if (!a || !b) {
        // Display message indicating weather check is in progress
        resultElement.innerText = "Checking the weather...";
    } else if (a.includes(b)) {
        // Display message indicating weather matches preferences
        resultElement.innerText = "The weather matches your preferences!";
    } else {
        // Display message indicating weather does not match preferences
        resultElement.innerText = "The weather does not match your preferences.";
    }
}

// Handle activity change and redirect if "Add New" is selected
function handleActivityChange() {
    const activitySelect = document.getElementById('activity');
    const selectedValue = activitySelect.value;

    if (selectedValue === 'add-new') {
        // Redirect to the new activity page if "Add New" is selected
        window.location.href = '/activities/new';
    } else {
        // Update activity details
        updateActivityDetails();
        toggleFormVisibility();
    }
}

// Function to check the activityId and toggle form visibility
function toggleFormVisibility() {
    const activityIdInput = document.getElementById("activityId");
    const bookingForm = document.getElementById("booking");

    var activityId = activityIdInput.value;
    if (activityId && activityId.trim() !== "") {
        bookingForm.style.display = "flex";
    } else {
        bookingForm.style.display = "none";
    }
}