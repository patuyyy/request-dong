const { Router } = require("express");
const RequestController = require("../controllers/request.controller");

const RequestRouter = Router();
const Request = new RequestController();

// GET ROUTES
RequestRouter.get("/", Request.getAll);
RequestRouter.get("/:request_id", Request.getById);

// POST ROUTES
RequestRouter.post("/addRequest/:user_id", Request.addRequest);
RequestRouter.post("/takeRequest/:user_id", Request.takeRequest);
RequestRouter.post("/finishRequest", Request.finishRequest);
RequestRouter.post("/rejectRequest", Request.rejectRequest);


 
// DELETE ROUTES


module.exports = RequestRouter;