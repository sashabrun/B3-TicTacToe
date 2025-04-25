# 🎮 Tic Tac Toe - Kotlin Android App

## 📱 Description

**Tic Tac Toe** est un jeu de morpion développé en **Kotlin** pour Android. Il permet de jouer :
- **En local (offline)** : Deux joueurs s'affrontent sur le même appareil.
- **En ligne (online)** : Deux joueurs se connectent à une session via un identifiant unique, en utilisant **Firebase Firestore**.

L'application se compose de **quatre pages principales** :
1. 🏠 **MainActivity** – Page d’accueil, choix du mode de jeu  
2. 📜 **RulesActivity** – Affichage des règles du jeu  
3. 🎮 **GameActivity** – Partie de Tic Tac Toe (local & online)  
4. 🕓 **MatchHistoryActivity** – Historique des parties (*partiellement implémenté*)

---

## 🧠 Architecture du projet

Le projet suit une **architecture MVC**, avec séparation claire entre les modèles, la logique de jeu et l’interface utilisateur.

### 🧩 Fichiers principaux

| Fichier                     | Description                                                         |
|----------------------------|----------------------------------------------------------------------|
| `MainActivity.kt`          | Page principale pour lancer une partie ou voir les règles            |
| `RulesActivity.kt`         | Affiche les règles du Tic Tac Toe                                    |
| `GameActivity.kt`          | Gère l'affichage et la logique d'une partie                          |
| `GameBoardView.kt`         | Composant graphique custom                                           |
| `GameModel.kt`             | Gère les états et règles du jeu                                      |
| `GameData.kt`              | Singleton pour stocker les données de partie                         |
| `MatchHistoryActivity.kt`  | Page affichant l’historique des parties (*partiel*)                  |
| `MatchHistoryAdapter.kt`   | Adapter de la liste des parties passées                              |
| `MatchHistoryItem.kt`      | Donnée d’un élément de l’historique                                  |
| `NotificationUtils.kt`     | Gestion des notifications et interactions système (*partiel*)        |

### 📁 Layouts XML

L’application contient de nombreux fichiers XML :
- `activity_main.xml`
- `activity_game.xml`
- `activity_rules.xml`
- `activity_match_history.xml`
- `match_history_item.xml`
- ...et d'autres encore pour les menus et composants.

---

## 🚀 Fonctionnalités

- 🧑‍🤝‍🧑 Mode local : deux joueurs sur le même téléphone
- 🌐 Mode en ligne via un identifiant unique
- ☁️ Sauvegarde/synchronisation avec Firebase Firestore
- 🎨 Plateau de jeu interactif avec `GameBoardView`
- 🕓 Historique des parties (en développement)
- 📳 Retour tactile, gestion du bouton retour
- 🔔 Notifications (en développement)
- 📡 Appels réseau Firebase
- 🧱 Architecture MVC avec patterns appliqués

---

## ✅ Validation des compétences demandées

| Compétence | Détail | État |
|-----------|--------|------|
| **Persistance de Données** | Firebase Firestore utilisé pour les données des parties en ligne | ✅ |
| **Changement et nombre d’activités/fragments** | 3 activités : `MainActivity`, `GameActivity`, `RulesActivity` | ✅ |
| **Gestion du bouton Back** | `OnBackPressedCallback` avec boîte de dialogue de confirmation | ✅ |
| **Affichage d'une liste avec adapter** | Fonction partiellement présente pour l’historique des parties | 🟡 |
| **Qualité du code** | Structure claire : vues, modèles, données bien séparés | ✅ |
| **Pertinence d'utilisation des layouts** | Bon usage de `LinearLayout`, `ConstraintLayout`, `GridLayout` | ✅ |
| **Qualité de l’interaction utilisateur** | Animations, transitions fluides, feedbacks utilisateurs | ✅ |
| **Composant graphique custom** | `GameBoardView` personnalisé, ligne gagnante animée | ✅ |
| **Tâches en background** | Firebase exécuté en tâche de fond | 🟡 |
| **Qualité de l’interface graphique** | Interface soignée, responsive, animée | ✅ |
| **Codage d’un menu** | Menu principal avec navigation vers les règles | ✅ |
| **Application de patterns** | `GameData` (Singleton), structure MVC bien appliquée | ✅ |
| **Demande d'autorisations** | Partiellement gérée dans certaines fonctionnalités | 🟡 |
| **Appel de WebServices** | Appels Firebase considérés comme web services | ✅ |
| **Utilisation d’API Android** | Animations, interactions tactiles (UX) | ✅ |

---

## 🛠️ Technologies utilisées

- **Langage** : Kotlin
- **Base de données** : Firebase Firestore
- **UI** : XML (layouts, menus, custom views)
- **Architecture** : MVC + Singleton
- **Outils** : Android Studio, Git, Firebase Console

---

## 🔜 Améliorations prévues

- 🔧 Finaliser l’historique des parties (Firebase + affichage)
- 🔧 Ajout de préférences utilisateur
- 🧠 Mode solo avec IA (futur)
- 🌍 Traduction multilingue (anglais, français...)

---

## 👨‍💻 Auteur

Projet réalisé par **Sasha BRUN** dans le cadre d’un projet Android Mobile Kotlin.  
📫 N'hésitez pas à cloner, tester et contribuer !

