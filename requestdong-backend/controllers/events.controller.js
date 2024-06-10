const { db } = require("../config/db.config");
const { buildResp, cleanStr } = require("../utils/utils");

class EventsController {
    async getAll(req, res) {
        try {
          const events = await db.query("SELECT * FROM events;");
          res
            .status(200)
            .send(events.rows);
        } catch (err) {
          console.error(err.message);
          return;
        }
    }

    async getById(req, res) {
        const { event_id } = req.params;
        try {
          const user = await db.query(
            `SELECT * FROM events WHERE event_id = ${event_id};`
          );
          const msg =
            user.rows.length === 0
              ? "Event not found"
              : "Event retrieved successfully";
          
          if(user.rows.length === 0) {
          res.status(400).send(buildResp(msg, user.rows[0]));
        } else {
          res.status(200).send(buildResp(msg, user.rows[0]));
        }

        } catch (err) {
          console.error(err.message);
          return;
        }
    }

    async addEvent(req, res) {
        const { name, description, time } = req.body;
        if (!name || !description || !time) {
          res.status(400).send(buildResp("Missing required fields"));
          return;
        }

        try {   
          const user = await db.query(
            `INSERT INTO events (name, description, time) 
            VALUES ('${name}', '${description}', '${time}') 
            RETURNING *;`
          );
          res
            .status(200)
            .send(buildResp("Event created successfully", user.rows[0]));
        } catch (err) {
          console.error(err.message);
          return;
        }
    } 

    


}

module.exports = EventsController;