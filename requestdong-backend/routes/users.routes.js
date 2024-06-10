const { Router } = require("express");
const UsersController = require("../controllers/users.controller");
const validateUser = require("../middleware/users.middleware");

const UsersRouter = Router();
const Users = new UsersController();

// GET ROUTES
UsersRouter.get("/", Users.getAll);
UsersRouter.get("/:user_id", Users.getById);
UsersRouter.get("/staffacara/:event_id", Users.getStaffAcara);
UsersRouter.get("/staffoperasional/:event_id", Users.getStaffOperasional);
UsersRouter.post("/checkifregistered", Users.checkIfRegistered);



// POST ROUTES
UsersRouter.post("/register", Users.register);
UsersRouter.post("/login", Users.login);
UsersRouter.post("/addtoacara", Users.addToAcara);
UsersRouter.post("/addtooperasional", Users.addToOperasional);


// DELETE ROUTES
UsersRouter.delete("/delete/:user_id", Users.deleteById);


module.exports = UsersRouter;