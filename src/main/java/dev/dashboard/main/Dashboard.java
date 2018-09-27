package dev.dashboard.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.logging.Logger;

import org.omg.CORBA.Principal;

import dev.dashboard.entities.Story;
import dev.dashboard.plan.SprintMapper;
import dev.dashboard.util.*;

public class Dashboard {

	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		final Logger LOGGER = Logger.getLogger(Dashboard.class.getName());

		Scanner in = new Scanner(System.in);

		String str = "-1";

		do {
			printMenu();
			try {
				str = in.nextLine();
				switch (str) {

				case "1":
					HibernateUtil.printDevelopers();
					break;
				case "2":
					new PrintSysOutImp("Enter Name for Creation");
					HibernateUtil.createDeveloper(in.next());
					break;
				case "3":
					new PrintSysOutImp("EnterID for Removal");
					HibernateUtil.removeDeveloper(in.nextLong());
					break;
				case "4":
					new PrintSysOutImp("Avaliable Releases :");
					HibernateUtil.getAvaliableReleases();
					new PrintSysOutImp("Enter Release: ");
					HibernateUtil.getUserStoriesForReleaseSortedByFeature(in.nextLine());
					break;
				case "5":
					new PrintSysOutImp("Enter Feature ID :");
					HibernateUtil.getEstimateForFeature(in.nextInt());
					break;
				case "6":
					new PrintSysOutImp("Plan US for Sprint - Enter US:");
					Long tempUSID = in.nextLong();
					
					new PrintSysOutImp("Enter Developer ID :");
					HibernateUtil.printDevelopers();
					Long devId = in.nextLong();
					new PrintSysOutImp("Enter Sprint Name :");
					String spName = in.next();
					
					HibernateUtil.planUserStoryForSprint(HibernateUtil.getStoryByID(tempUSID), HibernateUtil.getDevByID(devId), spName);
					break;
				default:
					break;

				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Wrong Selection! - Exiting cuz your STUPID");
				break;
			}

		} while (true );

		// SprintMapper.populateSprint();

		// HibernateUtil.createDeveloper(StringUtils.GenerateRandomName());
		// HibernateUtil.getDevelopers();
		// HibernateUtil.printDevelopers();
	}

	/**
	 * ("--- Welcome to Main Menu ---"); </br>
	 * ("--- 1: List all Developers ---");</br>
	 * ("--- 2: Add Developer ---");</br>
	 * ("--- 3: Remove Developer ---");</br>
	 * ("--- 4: List ---");</br>
	 * ("--- 0: END ---");</br>
	 */
	private static void printMenu() {
		System.out.println("--- Welcome to Main Menu ---");
		System.out.println("--- 1: List all Developers ---");
		System.out.println("--- 2: Add Developer ---");
		System.out.println("--- 3: Remove Developer ---");
		System.out.println("--- 4: List UserStories for Release---");
		System.out.println("--- 5: Estimate for Feature---");
		System.out.println("--- 6: Assign US to a Sprint/Developer---");

		System.out.println("--- 0: END ---");
	}

}