import express from 'express';
import bodyParser from 'body-parser';
import db from "./firebase.js";
import path from 'path';
import { fileURLToPath } from 'url';


const app = express();
const port = 3000;

const __filename = fileURLToPath(import.meta.url);  // Get the filename from the URL
const __dirname = path.dirname(__filename);  // Derive the dirname from the filename

app.use(bodyParser.urlencoded({ extended: true }));
app.set('view engine', 'ejs');
app.set('views', path.join(__dirname, 'views'));

app.get('/', (req, res) => {
    res.render("console", {message: "Input OTP"});
  });

app.post('/input', async (req, res) => {
    const pin = req.body.code;
    console.log(pin);
    if (!pin || pin.length !== 4) {
      res.render("console", {message : "Invalid OTP. Please enter a 4-digit OTP."});
    }
  
    try {
      // Check if the pin exists in the Firebase Realtime Database
      const ref = db.ref('code'); // Assuming the 'pins' node holds the PINs
      const snapshot = await ref.orderByValue().equalTo(pin).once('value');
  
      if (snapshot.exists()) {
        // If the PIN exists, get the key(s) and delete it
        snapshot.forEach((childSnapshot) => {
          const key = childSnapshot.key;
          const val = childSnapshot.val();
          generateAttendence(key,val);
          ref.child(key).remove(); // Remove the key and its value
        });
        res.render("console", {message: "Attendance Recorded ✅"});
      } else {
        res.render("console", {message: "Not Found. Request OTP Again"});
      }
    } catch (error) {
      console.error('Error checking/deleting PIN:', error);
      res.status(500).send('Server error');
    }
  });


function generateAttendence(uid,val) {
    const now = new Date();
    const formattedDate = now.toLocaleString();

    const ref = db.ref('student/'+ uid+'/attendance'); // 'pins' is the parent node where the data will be stored
    ref.child(val).set(formattedDate)
  .then(() => {
    console.log('Data written successfully!');
  })
  .catch((error) => {
    console.error('Error writing data:', error);
  });
}

app.listen(port, () => {
    console.log(`Server is running on http://localhost:${port}`);
  });
  