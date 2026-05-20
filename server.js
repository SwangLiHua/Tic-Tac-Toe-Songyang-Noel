const express = require("express");
const http = require("http");
const { Server } = require("socket.io");
const path = require("path");

const app = express();
const server = http.createServer(app);
const io = new Server(server);

app.use(express.static(path.join(__dirname, "public")));

const rooms = {};

function createEmptyBoard() {
  return [
    ["E", "E", "E"],
    ["E", "E", "E"],
    ["E", "E", "E"]
  ];
}

function checkWin(board, player) {
  for (let i = 0; i < 3; i++) {
    if (
      board[i][0] === player &&
      board[i][1] === player &&
      board[i][2] === player
    ) {
      return true;
    }

    if (
      board[0][i] === player &&
      board[1][i] === player &&
      board[2][i] === player
    ) {
      return true;
    }
  }

  if (
    board[0][0] === player &&
    board[1][1] === player &&
    board[2][2] === player
  ) {
    return true;
  }

  if (
    board[0][2] === player &&
    board[1][1] === player &&
    board[2][0] === player
  ) {
    return true;
  }

  return false;
}

function isDraw(board) {
  for (let r = 0; r < 3; r++) {
    for (let c = 0; c < 3; c++) {
      if (board[r][c] === "E") {
        return false;
      }
    }
  }

  return !checkWin(board, "X") && !checkWin(board, "O");
}

function sendRoomUpdate(roomCode) {
  const room = rooms[roomCode];

  if (!room) {
    return;
  }

  io.to(roomCode).emit("roomUpdate", {
    board: room.board,
    currentPlayer: room.currentPlayer,
    gameOver: room.gameOver,
    message: room.message,
    scores: room.scores,
    winner: room.winner
  });
}

io.on("connection", socket => {
  socket.on("createRoom", () => {
    const roomCode = Math.random().toString(36).substring(2, 7).toUpperCase();

    rooms[roomCode] = {
      board: createEmptyBoard(),
      currentPlayer: "X",
      players: [socket.id],
      gameOver: false,
      winner: null,
      message: "Waiting for Player O...",
      scores: {
        X: 0,
        O: 0
      }
    };

    socket.join(roomCode);

    socket.emit("roomCreated", {
      roomCode: roomCode,
      player: "X"
    });

    sendRoomUpdate(roomCode);
  });

  socket.on("joinRoom", data => {
    const roomCode = data.roomCode.toUpperCase();
    const room = rooms[roomCode];

    if (!room) {
      socket.emit("errorMessage", "Room not found.");
      return;
    }

    if (room.players.length >= 2) {
      socket.emit("errorMessage", "Room is full.");
      return;
    }

    room.players.push(socket.id);
    socket.join(roomCode);

    socket.emit("roomJoined", {
      roomCode: roomCode,
      player: "O"
    });

    room.message = "Player X's turn";
    sendRoomUpdate(roomCode);
  });

  socket.on("makeMove", data => {
    const roomCode = data.roomCode;
    const row = data.row;
    const col = data.col;
    const player = data.player;

    const room = rooms[roomCode];

    if (!room || room.gameOver) {
      return;
    }

    if (player !== room.currentPlayer) {
      return;
    }

    if (room.board[row][col] !== "E") {
      return;
    }

    room.board[row][col] = player;

    if (checkWin(room.board, player)) {
      room.gameOver = true;
      room.winner = player;
      room.scores[player]++;
      room.message = "Player " + player + " wins!";
      sendRoomUpdate(roomCode);
      return;
    }

    if (isDraw(room.board)) {
      room.gameOver = true;
      room.winner = "draw";
      room.message = "It's a draw!";
      sendRoomUpdate(roomCode);
      return;
    }

    room.currentPlayer = player === "X" ? "O" : "X";
    room.message = "Player " + room.currentPlayer + "'s turn";
    sendRoomUpdate(roomCode);
  });

  socket.on("newRound", data => {
    const room = rooms[data.roomCode];

    if (!room) {
      return;
    }

    room.board = createEmptyBoard();
    room.currentPlayer = "X";
    room.gameOver = false;
    room.winner = null;
    room.message = "Player X's turn";

    sendRoomUpdate(data.roomCode);
  });
});

const PORT = process.env.PORT || 3000;

server.listen(PORT, "0.0.0.0", () => {
  console.log("Server running on port " + PORT);
});