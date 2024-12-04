import admin from 'firebase-admin';

// Replace with the path to your service account JSON file
import serviceAccount from './myattendance-fe1f3-firebase-adminsdk-6blba-91a326a6d4.json' with { type: "json" } ;

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://myattendance-fe1f3-default-rtdb.asia-southeast1.firebasedatabase.app"
});

const db = admin.database();
export default db;

