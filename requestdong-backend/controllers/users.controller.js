const { db } = require("../config/db.config");
const bcrypt = require("bcrypt");
const { buildResp, cleanStr } = require("../utils/utils");
const { use } = require("../routes/request.routes");

class UsersController {
    async getAll(req, res) {
        try {
          const users = await db.query("SELECT * FROM users;");
          res
            .status(200)
            .send(buildResp("Users retrieved successfully", users.rows));
        } catch (err) {
          console.error(err.message);
          return;
        }
    }

    async getById(req, res) {
        const { user_id } = req.params;
        try {
          const user = await db.query(
            `SELECT * FROM users WHERE user_id = ${user_id};`
          );
          const msg =
            user.rows.length === 0
              ? "User not found"
              : "User retrieved successfully";
          
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

    async register(req, res) {
        const { name, username, password} = req.body;
        if (!name || !username || !password) {
          res.status(400).send(buildResp("Missing required fields"));
          return;
        }
    
        try {
          // Hash password
          const saltRounds = 10;
          const hashedPassword = await bcrypt.hash(password, saltRounds);
    
          const user = await db.query(
            `INSERT INTO users (name, username, password) 
            VALUES ('${name}', '${username}', '${hashedPassword}') 
            RETURNING *;`
          );
          res
            .status(200)
            .send(buildResp("User created successfully", user.rows[0]));
        } catch (err) {
          console.error(err.message);
          return;
        }
    }

    async login(req, res) {
        const { username, password } = req.body;
        if (!username || !password) {
          res
            .status(400)
            .send(buildResp("Missing required fields", { login: false }, false));
          return;
        }
    
        try {
          const user = await db.query(
            `SELECT * FROM users WHERE username = '${username}';`
          );
          if (user.rows.length === 0) {
            res
              .status(400)
              .send(buildResp("User not found", { login: false }, false));
            return;
          }
    
          const match = await bcrypt.compare(password, user.rows[0].password);
          if (!match) {
            res
              .status(400)
              .send(buildResp("Incorrect password", { login: false }, false));
            return;
          }
    
          res.status(200).send(buildResp("Login successful", user.rows[0]));
        } catch (err) {
          console.error(err.message);
          return;
        }
    }

    async deleteById(req, res) {
      const { user_id } = req.params;
      try {
        const user = await db.query(
          `DELETE FROM users WHERE user_id = ${user_id} RETURNING *;`
        );
        const msg =
          user.rows.length === 0 ? "User not found" : "User deleted successfully";
        
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

    async addToAcara(req, res) {
      const { event_id, user_id } = req.body;
  
      try {
        const user = await db.query(
          `INSERT INTO staff_acara (user_id, event_id) 
          VALUES ('${user_id}', '${event_id}') 
          RETURNING *;`
        );
        res
          .status(200)
          .send(buildResp("Successfully added to staff acara", user.rows[0]));
      } catch (err) {
        console.error(err.message);
        return;
      }
  }

  async addToOperasional(req, res) {
    const { event_id, user_id } = req.body;

    try {
      const user = await db.query(
        `INSERT INTO staff_operasional (user_id, event_id) 
        VALUES ('${user_id}', '${event_id}') 
        RETURNING *;`
      );
      res
        .status(200)
        .send(buildResp("Successfully added to staff operasional", user.rows[0]));
    } catch (err) {
      console.error(err.message);
      return;
    }
}

async getStaffAcara(req, res) {
  const { event_id } = req.params;
  const { user_id } = req.body;
  try {
    const users = await db.query(
      `SELECT 
              p.p_acara_id,
              u.user_id,
              e.event_id,
              u.username,
              e.name as event_name
              FROM users AS u
              INNER JOIN
              staff_acara AS p
              ON u.user_id = p.user_id
              INNER JOIN
              events as e
              ON p.event_id = e.event_id
              AND e.event_id = ${event_id}
	            AND u.user_id = ${user_id};`
    );
    res
      .status(200)
      .send(buildResp("Users retrieved successfully", users.rows));
  } catch (err) {
    console.error(err.message);
    return;
  }
}

async getStaffOperasional(req, res) {
  const { event_id } = req.params;
  try {
    const users = await db.query(
      `SELECT 
              p.p_ops_id,
              u.user_id,
              e.event_id,
              u.name,
              e.name as event_name
              FROM users AS u
              INNER JOIN
              staff_operasional AS p
              ON u.user_id = p.user_id
              INNER JOIN
              events as e
              ON p.event_id = e.event_id
              AND e.event_id = ${event_id};`
    );
    res
      .status(200)
      .send(buildResp("Users retrieved successfully", users.rows));
  } catch (err) {
    console.error(err.message);
    return;
  }
}

async checkIfRegistered (req, res) {
  const { event_id, user_id } = req.body;
  
  try {
    const users = await db.query(
      `SELECT 
    sa.p_acara_id, 
    NULL AS p_ops_id, 
    u.name AS username, 
    e.name AS event_name, 
    sa.user_id, 
    sa.event_id
FROM 
    staff_acara sa
JOIN 
    users u ON sa.user_id = u.user_id
JOIN 
    events e ON sa.event_id = e.event_id
WHERE 
    sa.event_id = ${event_id} AND sa.user_id = ${user_id}

UNION ALL

SELECT 
    NULL AS p_acara_id, 
    so.p_ops_id, 
    u.name AS username, 
    e.name AS event_name, 
    so.user_id, 
    so.event_id
FROM 
    staff_operasional so
JOIN 
    users u ON so.user_id = u.user_id
JOIN 
    events e ON so.event_id = e.event_id
WHERE 
    so.event_id = ${event_id} AND so.user_id = ${user_id};
`
    );
    res
      .status(200)
      .send(users.rows);
  } catch (err) {
    console.error(err.message);
    return;
  }
}

    
}

module.exports = UsersController;