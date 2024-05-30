const { Router } = require("express");
const EventsController = require("../controllers/events.controller");

const EventsRouter = Router();
const Events = new EventsController();

// GET ROUTES
EventsRouter.get("/", Events.getAll);
EventsRouter.get("/:event_id", Events.getById);

// POST ROUTES
EventsRouter.post("/addEvent", Events.addEvent);

// DELETE ROUTES
//EventsRouter.delete("/delete/:user_id", Events.deleteById);

module.exports = EventsRouter;