const { db } = require("../config/db.config");
const { buildResp, cleanStr } = require("../utils/utils");

class RequestController {
    async getAll(req, res) {
        try {
          const request = await db.query("SELECT * FROM requests;");
          res
            .status(200)
            .send(buildResp("Request retrieved successfully", request.rows));
        } catch (err) {
          console.error(err.message);
          return;
        }
    }

    async getById(req, res) {
        const { request_id } = req.params;
        try {
          const request = await db.query(
            `SELECT * FROM requests WHERE request_id = ${request_id};`
          );
          const msg =
          request.rows.length === 0
              ? "Request not found"
              : "Request retrieved successfully";
          
          if(request.rows.length === 0) {
          res.status(400).send(buildResp(msg, request.rows[0]));
        } else {
          res.status(200).send(buildResp(msg, request.rows[0]));
        }

        } catch (err) {
          console.error(err.message);
          return;
        }
    }

    async getByEvent(req, res) {
      const { event_id } = req.params;
      try {
        const request = await db.query(
          `select r.request_id as request_id, 
          u.name as request_by, 
          i.name as taken_by, 
          e.name as event_name, 
          r.requested_thing, 
          r.amount, 
          r.deadline, 
          r.status 
          from requests as r 
          inner join users as u 
          on u.user_id = r.request_by 
          inner join users as i 
          on i.user_id = r.taken_by 
          inner join events as e
          on e.event_id = r.event;`
        );
        const msg =
        request.rows.length === 0
            ? "Request not found"
            : "Request retrieved successfully";
        
        if(request.rows.length === 0) {
        res.status(400).send(buildResp(msg, request.rows));
      } else {
        res.status(200).send(buildResp(msg, request.rows));
      }

      } catch (err) {
        console.error(err.message);
        return;
      }
  }

    

    async addRequest(req, res) {
      const { user_id } = req.params;
      const { event, requested_thing, amount, deadline } = req.body;
      if (!requested_thing || !amount || !deadline) {
        res.status(400).send(buildResp("Missing required fields"));
        return;
      }

      try {   
        const request = await db.query(
          `INSERT INTO requests (request_by, event, requested_thing, amount, deadline) 
          VALUES ('${user_id}', '${event}', '${requested_thing}', '${amount}', '${deadline}') 
          RETURNING *;`
        );
        res
          .status(200)
          .send(buildResp("Event created successfully", request.rows[0]));
      } catch (err) {
        console.error(err.message);
        return;
      }
    }

    async takeRequest(req, res) {
      const { user_id } = req.params;
      const { request_id } = req.body;

      try {   
        const request = await db.query(
          `UPDATE requests SET taken_by = ${user_id}, status = 'onprogress'
          WHERE request_id = ${request_id} 
          RETURNING *;`
        );
        res
          .status(200)
          .send(buildResp("Request taken successfully", request.rows[0]));
      } catch (err) {
        console.error(err.message);
        return;
      }
    }

    async finishRequest(req, res) {
      const { request_id } = req.body;

      try {   
        const request = await db.query(
          `UPDATE requests SET status = 'finish'
          WHERE request_id = ${request_id} 
          RETURNING *;`
        );
        res
          .status(200)
          .send(buildResp("Request finished", request.rows[0]));
      } catch (err) {
        console.error(err.message);
        return;
      }
    }
    
    async rejectRequest(req, res) {
      const { request_id } = req.body;

      try {   
        const request = await db.query(
          `UPDATE requests SET status = 'rejected'
          WHERE request_id = ${request_id} 
          RETURNING *;`
        );
        res
          .status(200)
          .send(buildResp("Request rejected", request.rows[0]));
      } catch (err) {
        console.error(err.message);
        return;
      }
    }
}

module.exports = RequestController;