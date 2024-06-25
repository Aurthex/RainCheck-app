function updateActivityDetails() {
    const selectElement = document.getElementById("activity");
    const selectedOption = selectElement.options[selectElement.selectedIndex];
    const activityData = selectedOption.getAttribute('data-activity');

    if (activityData) {
        const selectedActivity = JSON.parse(activityData);

        document.getElementById("activityDescription").innerText = selectedActivity.description;
        document.getElementById("activityTemp").innerText = selectedActivity.temperature;
        document.getElementById("activityWindSpeed").innerText = selectedActivity.windSpeed;
        document.getElementById("activityScore").innerText = selectedActivity.score;
        document.getElementById("activityScoreMessage").innerText = selectedActivity.scoreMessage;

        // Display conditions
        const conditions = selectedActivity.conditions.map(cond => cond.name).join(", ");
        document.getElementById("activityCond").innerText = conditions;

    } else {
        document.getElementById("activityDescription").innerText = '';
        document.getElementById("activityTemp").innerText = '';
        document.getElementById("activityWindSpeed").innerText = '';
        document.getElementById("activityScore").innerText = '';
        document.getElementById("activityScoreMessage").innerText = '';
        document.getElementById("activityCond").innerText = '';
    }
}

function checkStringContainment(a, b, resultElementId) {
    const resultElement = document.getElementById(resultElementId);
    if (!a || !b) {
        resultElement.innerText = "Checking the weather...";
    } else if (a.includes(b)) {
        resultElement.innerText = "The weather matches your preferences!";
    } else {
        resultElement.innerText = "The weather does not match your preferences.";
    }
}

// Handle activity change and redirect if "Add New" is selected
function handleActivityChange() {
    const activitySelect = document.getElementById('activity');
    const selectedValue = activitySelect.value;


    if (selectedValue === 'add-new') {
        window.location.href = '/activities/new';
    } else {
        updateActivityDetails();
        updateContainmentMessage();
    }
}