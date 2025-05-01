document.getElementById('recommendationForm').addEventListener('submit', function(event) {
    event.preventDefault();
  
    const name = document.getElementById('name').value;
    const courses = document.getElementById('courses').value;
    const interest = document.getElementById('interest').value;
  
    // Simulated result (in real case, send to backend and get result)
    const resultDiv = document.getElementById('result');
    resultDiv.innerHTML = `
      <strong>Hello ${name}!</strong><br>
      Based on your interest in <em>${interest}</em> and completed courses, we recommend:<br><br>
      <ul>
        <li>Advanced ${interest} Techniques</li>
        <li>${interest} with Python</li>
        <li>Project Building in ${interest}</li>
      </ul>
    `;
    resultDiv.style.display = 'block';
  });
  