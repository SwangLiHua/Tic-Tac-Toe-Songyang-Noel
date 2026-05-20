import { io } from "https://cdn.socket.io/4.8.1/socket.io.esm.min.js";

import { initializeApp } from "https://www.gstatic.com/firebasejs/10.12.5/firebase-app.js";

import {
  getDatabase,
  ref,
  get,
  set,
  onValue
} from "https://www.gstatic.com/firebasejs/10.12.5/firebase-database.js";

const socket = io();

const firebaseConfig = {
  apiKey: "AIzaSyD0nS-Fp1qbKXfuzZgbBG06htLk9PZghSQ",
  authDomain: "tictactoes-2bc27.firebaseapp.com",
  databaseURL: "https://tictactoes-2bc27-default-rtdb.firebaseio.com",
  projectId: "tictactoes-2bc27",
  storageBucket: "tictactoes-2bc27.firebasestorage.app",
  messagingSenderId: "723765050127",
  appId: "1:723765050127:web:3f3b4bc9554ccde48a5c7a",
  measurementId: "G-JH4Y4CZVQZ"
};

const firebaseApp = initializeApp(firebaseConfig);
const database = getDatabase(firebaseApp);

const menu = document.getElementById("menu");
const game = document.getElementById("game");

const localBtn = document.getElementById("localBtn");
const consoleBtn = document.getElementById("consoleBtn");
const onlineBtn = document.getElementById("onlineBtn");
const joinBtn = document.getElementById("joinBtn");

const roomInput = document.getElementById("roomInput");

const roomText = document.getElementById("roomText");
const playerText = document.getElementById("playerText");
const statusText = document.getElementById("statusText");
const scoreText = document.getElementById("scoreText");

const cells = document.querySelectorAll(".cell");
const normalBoard = document.getElementById("board");
const consoleBoard = document.getElementById("consoleBoard");

const newRoundBtn = document.getElementById("newRoundBtn");
const menuBtn = document.getElementById("menuBtn");

const winScreen = document.getElementById("winScreen");
const winTitle = document.getElementById("winTitle");
const winMessage = document.getElementById("winMessage");
const playAgainBtn = document.getElementById("playAgainBtn");
const winMenuBtn = document.getElementById("winMenuBtn");

const leaderboardList = document.getElementById("leaderboardList");

let mode = "local";
let roomCode = null;
let myPlayer = "X";

let board = createEmptyBoard();
let currentPlayer = "X";
let gameOver = false;

let scores = {
  X: 0,
  O: 0
};

let lastSavedWinnerKey = "";

function showGame() {
  menu.classList.add("hidden");
  game.classList.remove("hidden");
}

function showMenu() {
  game.classList.add("hidden");
  menu.classList.remove("hidden");
  winScreen.classList.add("hidden");
}

function createEmptyBoard() {
  return [
    ["E", "E", "E"],
    ["E", "E", "E"],
    ["E", "E", "E"]
  ];
}

function playClickSound() {
  const audio = new AudioContext();
  const oscillator = audio.createOscillator();
  const gain = audio.createGain();

  oscillator.connect(gain);
  gain.connect(audio.destination);

  oscillator.frequency.value = 500;
  gain.gain.value = 0.05;

  oscillator.start();
  oscillator.stop(audio.currentTime + 0.08);
}

function playWinSound() {
  const audio = new AudioContext();
  const oscillator = audio.createOscillator();
  const gain = audio.createGain();

  oscillator.connect(gain);
  gain.connect(audio.destination);

  oscillator.frequency.value = 850;
  gain.gain.value = 0.07;

  oscillator.start();
  oscillator.stop(audio.currentTime + 0.22);
}

function renderBoard(newBoard) {
  cells.forEach(cell => {
    const row = Number(cell.dataset.row);
    const col = Number(cell.dataset.col);
    const value = newBoard[row][col];

    cell.textContent = value === "E" ? "" : value;
    cell.classList.remove("x", "o");

    if (value === "X") {
      cell.classList.add("x");
    } else if (value === "O") {
      cell.classList.add("o");
    }
  });

  renderConsoleBoard(newBoard);
}

function renderConsoleBoard(newBoard) {
  let text = "";

  text += "   0   1   2\n";
  text += "  -----------\n";

  for (let r = 0; r < 3; r++) {
    text += r + " | ";

    for (let c = 0; c < 3; c++) {
      let value = newBoard[r][c];

      if (value === "E") {
        value = "-";
      }

      text += value + " | ";
    }

    text += "\n";
    text += "  -----------\n";
  }

  consoleBoard.textContent = text;
}

function checkWin(testBoard, player) {
  for (let i = 0; i < 3; i++) {
    if (
      testBoard[i][0] === player &&
      testBoard[i][1] === player &&
      testBoard[i][2] === player
    ) {
      return true;
    }

    if (
      testBoard[0][i] === player &&
      testBoard[1][i] === player &&
      testBoard[2][i] === player
    ) {
      return true;
    }
  }

  if (
    testBoard[0][0] === player &&
    testBoard[1][1] === player &&
    testBoard[2][2] === player
  ) {
    return true;
  }

  if (
    testBoard[0][2] === player &&
    testBoard[1][1] === player &&
    testBoard[2][0] === player
  ) {
    return true;
  }

  return false;
}

function isDraw(testBoard) {
  for (let r = 0; r < 3; r++) {
    for (let c = 0; c < 3; c++) {
      if (testBoard[r][c] === "E") {
        return false;
      }
    }
  }

  return !checkWin(testBoard, "X") && !checkWin(testBoard, "O");
}

function updateScoreText() {
  scoreText.textContent = "X: " + scores.X + " | O: " + scores.O;
}

function cleanName(name) {
  return name.trim().toLowerCase().replace(/[^a-z0-9]/g, "_");
}

async function saveWinToFirebase(winner) {
  if (winner === "draw") {
    return;
  }

  let playerName = prompt("Enter winner name for leaderboard:");

  if (playerName === null || playerName.trim() === "") {
    playerName = "Player_" + winner;
  }

  const safeName = cleanName(playerName);
  const playerRef = ref(database, "leaderboard/" + safeName);

  const snapshot = await get(playerRef);

  if (snapshot.exists()) {
    const oldData = snapshot.val();

    await set(playerRef, {
      name: oldData.name,
      wins: oldData.wins + 1
    });
  } else {
    await set(playerRef, {
      name: playerName,
      wins: 1
    });
  }
}

function loadLeaderboard() {
  const leaderboardRef = ref(database, "leaderboard");

  onValue(leaderboardRef, snapshot => {
    leaderboardList.innerHTML = "";

    if (!snapshot.exists()) {
      leaderboardList.innerHTML = "<li>No scores yet</li>";
      return;
    }

    const data = snapshot.val();
    const players = [];

    for (const key in data) {
      players.push(data[key]);
    }

    players.sort((a, b) => b.wins - a.wins);

    for (let i = 0; i < players.length && i < 5; i++) {
      const li = document.createElement("li");
      li.textContent = players[i].name + ": " + players[i].wins + " wins";
      leaderboardList.appendChild(li);
    }
  });
}

function showWinScreen(winner, shouldSaveToFirebase) {
  if (winner === "draw") {
    winTitle.textContent = "It's a Draw!";
    winMessage.textContent = "Nobody won this round.";
  } else {
    winTitle.textContent = "Player " + winner + " Wins!";
    winMessage.textContent = "Great game! Start a new round to play again.";
  }

  winScreen.classList.remove("hidden");
  playWinSound();

  if (shouldSaveToFirebase) {
    saveWinToFirebase(winner);
  }
}

function startLocalGame(selectedMode) {
  mode = selectedMode;
  roomCode = null;
  myPlayer = "X";

  board = createEmptyBoard();
  currentPlayer = "X";
  gameOver = false;
  lastSavedWinnerKey = "";

  scores = {
    X: 0,
    O: 0
  };

  if (selectedMode === "console") {
    roomText.textContent = "Mode: Console Style";
    playerText.textContent = "Console display on top. Click the grid below to move.";

    consoleBoard.classList.remove("hidden");
    normalBoard.classList.remove("hidden");
  } else {
    roomText.textContent = "Mode: Local Player vs Player";
    playerText.textContent = "Two players on one device";

    consoleBoard.classList.add("hidden");
    normalBoard.classList.remove("hidden");
  }

  statusText.textContent = "Player X's turn";

  updateScoreText();
  renderBoard(board);

  winScreen.classList.add("hidden");
  showGame();
}

function handleLocalMove(row, col) {
  if (gameOver) {
    return;
  }

  if (board[row][col] !== "E") {
    return;
  }

  board[row][col] = currentPlayer;
  playClickSound();
  renderBoard(board);

  if (checkWin(board, currentPlayer)) {
    gameOver = true;
    scores[currentPlayer]++;
    statusText.textContent = "Player " + currentPlayer + " wins!";
    updateScoreText();
    showWinScreen(currentPlayer, true);
    return;
  }

  if (isDraw(board)) {
    gameOver = true;
    statusText.textContent = "It's a draw!";
    showWinScreen("draw", false);
    return;
  }

  currentPlayer = currentPlayer === "X" ? "O" : "X";
  statusText.textContent = "Player " + currentPlayer + "'s turn";
}

function resetLocalRound() {
  board = createEmptyBoard();
  currentPlayer = "X";
  gameOver = false;
  lastSavedWinnerKey = "";
  statusText.textContent = "Player X's turn";
  renderBoard(board);
  winScreen.classList.add("hidden");
}

function resetOnlineRound() {
  socket.emit("newRound", {
    roomCode: roomCode
  });

  lastSavedWinnerKey = "";
  winScreen.classList.add("hidden");
}

localBtn.addEventListener("click", () => {
  startLocalGame("local");
});

consoleBtn.addEventListener("click", () => {
  startLocalGame("console");
});

onlineBtn.addEventListener("click", () => {
  mode = "online";
  socket.emit("createRoom");
});

joinBtn.addEventListener("click", () => {
  const code = roomInput.value.trim().toUpperCase();

  if (code.length === 0) {
    alert("Enter a room code first.");
    return;
  }

  mode = "online";
  socket.emit("joinRoom", {
    roomCode: code
  });
});

cells.forEach(cell => {
  cell.addEventListener("click", () => {
    const row = Number(cell.dataset.row);
    const col = Number(cell.dataset.col);

    if (mode === "local" || mode === "console") {
      handleLocalMove(row, col);
    } else {
      socket.emit("makeMove", {
        roomCode: roomCode,
        row: row,
        col: col,
        player: myPlayer
      });
    }
  });
});

newRoundBtn.addEventListener("click", () => {
  if (mode === "local" || mode === "console") {
    resetLocalRound();
  } else {
    resetOnlineRound();
  }
});

menuBtn.addEventListener("click", showMenu);

playAgainBtn.addEventListener("click", () => {
  if (mode === "local" || mode === "console") {
    resetLocalRound();
  } else {
    resetOnlineRound();
  }
});

winMenuBtn.addEventListener("click", showMenu);

socket.on("roomCreated", data => {
  roomCode = data.roomCode;
  myPlayer = data.player;

  board = createEmptyBoard();
  gameOver = false;
  lastSavedWinnerKey = "";

  normalBoard.classList.remove("hidden");
  consoleBoard.classList.add("hidden");

  roomText.textContent = "Room: " + roomCode;
  playerText.textContent = "You are X. Send this code to your friend.";
  statusText.textContent = "Waiting for Player O...";

  renderBoard(board);
  winScreen.classList.add("hidden");
  showGame();
});

socket.on("roomJoined", data => {
  roomCode = data.roomCode;
  myPlayer = data.player;

  gameOver = false;
  lastSavedWinnerKey = "";

  normalBoard.classList.remove("hidden");
  consoleBoard.classList.add("hidden");

  roomText.textContent = "Room: " + roomCode;
  playerText.textContent = "You are O";
  statusText.textContent = "Player X's turn";

  winScreen.classList.add("hidden");
  showGame();
});

socket.on("roomUpdate", data => {
  board = data.board;
  gameOver = data.gameOver;
  scores = data.scores;

  renderBoard(data.board);

  statusText.textContent = data.message;
  scoreText.textContent = "X: " + data.scores.X + " | O: " + data.scores.O;

  if (data.gameOver && data.winner) {
    const winnerKey = roomCode + "-" + data.winner + "-" + data.scores.X + "-" + data.scores.O;

    if (winnerKey !== lastSavedWinnerKey) {
      lastSavedWinnerKey = winnerKey;

      if (mode === "online" && myPlayer === "X") {
        showWinScreen(data.winner, true);
      } else {
        showWinScreen(data.winner, false);
      }
    }
  }
});

socket.on("errorMessage", message => {
  alert(message);
});

loadLeaderboard();