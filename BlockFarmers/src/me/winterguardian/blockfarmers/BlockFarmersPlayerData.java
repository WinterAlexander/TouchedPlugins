package me.winterguardian.blockfarmers;

import me.winterguardian.blockfarmers.state.BlockFarmersFarmingState;
import me.winterguardian.core.Core;
import me.winterguardian.core.game.PlayerData;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class BlockFarmersPlayerData extends PlayerData
{
	private int id;
	private BlockFarmersFarmingState state;
	
	private int area;
	private long lastFarm;
	private boolean first;
	
	public BlockFarmersPlayerData(BlockFarmersFarmingState state, int id, OfflinePlayer player)
	{
		super(player);
		this.state = state;
		this.id = id;
		this.first = true;
	}
	
	public void prepare()
	{
		if(getPlayer().isOnline())
			getPlayer().sendMessage(BlockFarmersMessage.FARMING_COLOR.toString().replace("<color>", getColor().getName()));

		if(getPlayer().hasPermission(BlockFarmersPlugin.DIAMOND_HOE))
			getPlayer().getInventory().addItem(new ItemStack(Material.DIAMOND_HOE, 1));

		if(getPlayer().hasPermission(BlockFarmersPlugin.IRON_HOE))
			getPlayer().getInventory().addItem(new ItemStack(Material.IRON_HOE, 1));

		if(getPlayer().hasPermission(BlockFarmersPlugin.GOLD_HOE))
			getPlayer().getInventory().addItem(new ItemStack(Material.GOLD_HOE, 1));

		if(getPlayer().hasPermission(BlockFarmersPlugin.STONE_HOE))
			getPlayer().getInventory().addItem(new ItemStack(Material.STONE_HOE, 1));

		getPlayer().getInventory().addItem(new ItemStack(Material.WOOD_HOE, 1));
	}
	
	public void end(boolean win, int players)
	{
		if(!getPlayer().isOnline())
			return;

		FarmersStats stats = new FarmersStats(getPlayer(), Core.getUserDatasManager().getUserData(getPlayer()));
		stats.addGamePoints(win ? (players - 1) * 25 : 10);
		if(win)
			stats.setVictories(stats.getVictories() + 1);
		stats.setGamesPlayed(stats.getGamesPlayed() + 1);
		stats.setTilesFarmed(stats.getTilesFarmed() + area);
	}

	public FarmersColor getColor()
	{
		return FarmersColor.values()[id];
	}

	public int getArea()
	{
		return area;
	}

	public void farm(Block block)
	{
		this.area++;
		this.lastFarm = System.currentTimeMillis();
		this.first = false;
		state.addBlockToRegen(block);
		getColor().getBlock().apply(block);
		/*
		List<LinkedList<Block>> tracks = new ArrayList<LinkedList<Block>>();
		List<LinkedList<Block>> closedPolygons = new ArrayList<LinkedList<Block>>();
		
		tracks.add(new LinkedList<Block>(Arrays.asList(block))); //nouveau chemin depuis le bloc cliqué
		
		while(tracks.size() != 0) //tant qu'il reste des chemins
		{
			LinkedList<Block> track = tracks.get(0); //on prend le premier chemin
			Block currentBlock = track.getLast(); //on prend le dernier block de ce chemin
			
			List<BlockFace> matchingFaces = new ArrayList<BlockFace>(); //toutes les directions vers lesquels il rencontre un de sa couleur
			
			for(BlockFace face : Arrays.asList(new BlockFace[]{BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST}))
				if(getColor().getBlock().match(currentBlock.getRelative(face)))
					matchingFaces.add(face);
			
			int index;
			if((index = track.indexOf(currentBlock)) > 0) //si notre bloc actuel n'est pas le bloc cliqué
				matchingFaces.remove(currentBlock.getFace(track.get(index - 1))); //on retire de où il provient
			else if(matchingFaces.size() < 2) //si y'a moins de 2 endroits où aller
				return; //cul de sac ou bloc seul
			
			if(matchingFaces.size() == 0) //s'il n'a plus endroit où aller
			{
				tracks.remove(track); 
				continue;
			}
			
			for(index = 0; index < matchingFaces.size(); index++) //pour toutes les endroits où il peut aller
			{
				if(track.contains(currentBlock.getRelative(matchingFaces.get(index)))) //s'il se touche lui même
				{
					if(currentBlock.getRelative(matchingFaces.get(index)).equals(block)) //s'il touche au bloc de base
						closedPolygons.add(track); //on a un vrai polygone
					tracks.remove(track); //CRASH ICI
					continue;
				}
				
				if(index == matchingFaces.size() - 1)//si c'est la dernière face à traiter
				{
					track.add(currentBlock.getRelative(matchingFaces.get(index))); //on avance
					break;
				}
				
				//sinon
				
				
				//on crée une nouvelle possibilité et on avance
				LinkedList<Block> newTrack = new LinkedList<Block>(track);
				newTrack.add(currentBlock.getRelative(matchingFaces.get(index)));
				tracks.add(newTrack);
			}
		}

		//compteur de polygones
		getPlayer().sendMessage("Polygons: " + closedPolygons.size());
		
		for(LinkedList<Block> polygon : closedPolygons) //pour chaque
		{
			int xPos[] = new int[polygon.size()], zPos[] = new int[polygon.size()]; 
			
			for(int i = 0; i < polygon.size(); i++)
			{
				xPos[i] = polygon.get(i).getX() * 10;
				zPos[i] = polygon.get(i).getZ() * 10;
			}
			
			Polygon poly = new Polygon(xPos, zPos, polygon.size()); //on crée un nouveau polygone awt
			
			List<Block> contained = new ArrayList<Block>(); //on prend une liste de blocs que contiendra chaque polygone
			
			int minX = MathUtil.getMin(xPos), maxX = MathUtil.getMax(xPos); //les minimums et maximums pour les boucles
			int minZ = MathUtil.getMin(zPos), maxZ = MathUtil.getMax(zPos);
			
			for(int x = minX; x <= maxX; x += 10) //on prend toutes la région du polygone
				for(int z = minZ; z <= maxZ; z += 10)
				{
					Rectangle rec = new Rectangle(); //pour éviter d'inclure les blocs sur les bords
					rec.add(x - 1, z - 1);
					rec.add(x - 1, z + 1);
					rec.add(x + 1, z + 1);
					rec.add(x + 1, z - 1);
					if(poly.contains(rec)) //si le bloc est à l'intérieur
						contained.add(block.getWorld().getBlockAt(x / 10, block.getY(), z / 10)); //il est contenu
				}
			
			boolean doContinue = false;
			
			for(Block containedBlock : contained) //pour tout les blocs
			{
				if(!((BlockFarmersConfig) state.getGame().getConfig()).canFarm(containedBlock)) //s'il ne pourrait pas en transformer un seul manuellement
				{
					doContinue = true; 
					break;
				}
			}
			
			for(Block containedBlock : contained)
			{
				state.addBlockToRegen(containedBlock);
				FarmersColor.BLUE.getBlock().apply(containedBlock);
			}
			
			//on quitte
			if(doContinue || contained.size() == 0)
				continue;
			
			//compteur de blocs
			getPlayer().sendMessage("  Blocks: " + contained.size());
			
			//je ne me suis jamais rendu ici...
			for(Block containedBlock : contained)
			{
				state.addBlockToRegen(containedBlock);
				getColor().getBlock().apply(containedBlock);
			}
		}
		
		
		
		
		/*List<LinkedList<Block>> polygons = new ArrayList<LinkedList<Block>>();
		List<LinkedList<Block>> closedPolygons = new ArrayList<LinkedList<Block>>();
		
		polygons.add(new LinkedList<Block>(Arrays.asList(block)));
		System.out.println("test0");
		while(polygons.size() != 0) //tant qu'il reste des polygons à traiter
		{
			System.out.println("test1");
			LinkedList<Block> currentPolygon = polygons.get(0); //le polygon que l'on traitera sera le premier
			Block currentBlock = currentPolygon.getLast(); //nous partirons du dernier bloc
			
			boolean matched = false; //il n'a pas trouvé de semblable aux alentours (pas encore)
			
			//toutes les directions dans lesquels il peut aller
			List<BlockFace> faces = new ArrayList<BlockFace>(Arrays.asList(new BlockFace[]{BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH}));
			
			//son index dans le polygon
			int index;
			if((index = currentPolygon.indexOf(currentBlock)) > 0) //s'il n'est pas premier
				faces.remove(currentBlock.getFace(currentPolygon.get(index - 1))); //on retire la direction de où il provient
			
			for(BlockFace face : faces) //pour toutes les directions
			{
				System.out.println("test2 " + face.name());
				if(getColor().getBlock().match(currentBlock.getRelative(face))) //si il trouve un semblable
				{
					System.out.println("test3");
					if(!matched) //si c'est son premier
					{
						System.out.println("test4");
						List<LinkedList<Block>> toRemove = new ArrayList<LinkedList<Block>>(); //tout ceux qui se seront trouvé une issue
						boolean foundPoly = false;
						for(LinkedList<Block> polygon : polygons) //pour tout les polygones
						{
							System.out.println("test5");
							if(polygon == currentPolygon) //si c'est le même 
								continue; //on s'en va
							System.out.println("test6");
							if(polygon.contains(currentBlock.getRelative(face))) //si le polygone contient notre bloc
							{
								System.out.println("test7");
								LinkedList<Block> closedPolygon = new LinkedList<Block>(); //on crée un nouveau polygone fermé
								LinkedList<Block> copy = new LinkedList<Block>(currentPolygon); //on fait une copie du premier
								LinkedList<Block> reversedCopy = new LinkedList<Block>(polygon); //on fait une copie de l'autre 
								
								while(copy.get(1) == reversedCopy.get(1)) //tant qu'ils auront une queue
								{
									System.out.println("test8");
									copy.remove(0); //on leur retire le bout de la queue
									reversedCopy.remove(0); 
								}
								reversedCopy.remove(0); //on lui retire qu'à lui le bout de la queue pour éviter un dédoublement du premier bloc
								System.out.println("test9");
								Collections.reverse(reversedCopy); //on la renverse 
								closedPolygon.addAll(copy); //on y ajoute tout le polygone actuel
								closedPolygon.addAll(reversedCopy); //on l'ajoute à la fin
								System.out.println("test10");
								if(closedPolygon.size() > 4) //si c'est pas un simple 2x2 impossible à remplir
									closedPolygons.add(closedPolygon); //le polygone est terminé
								System.out.println("test11");
								toRemove.add(currentPolygon); //plus besoin, cette partie de polygone a trouvée sa sortie et tout autres possibilités sont gérés par le sinon
								toRemove.add(polygon); //même chose
								foundPoly = true;
							}
							System.out.println("test12");
						}
						
						if(!foundPoly)
						{
							System.out.println("test17");
							currentPolygon.addLast(currentBlock.getRelative(face));
						}
						
						polygons.removeAll(toRemove);
						matched = true;
						System.out.println("test13");
					}
					else //sinon 
					{
						System.out.println("test14");
						LinkedList<Block> copy = new LinkedList<Block>(currentPolygon); //on crée une nouvelle possibilitée 
						copy.addLast(currentBlock.getRelative(face));
						polygons.add(copy); //qui sera traitée plus tard
					}
				}
			}
			
			if(!matched) //s'il n'a rien trouvé, c'est un cul de sac
			{
				System.out.println("test15");
				polygons.remove(currentPolygon); // on le retire
			}
		}
		System.out.println("test16");
		//DEBUG
		for(LinkedList<Block> polygon : closedPolygons)
			for(Block current : polygon)
				ParticleUtil.playSimpleParticles(current.getLocation().add(0.5, 1.5, 0.5), ParticleType.NOTE, 0, 0, 0, 0, 1);
		/*
		for(LinkedList<Block> polygon : closedPolygons)
		{
			int xPos[] = new int[polygon.size()], zPos[] = new int[polygon.size()];
			
			for(int i = 0; i < polygon.size(); i++)
			{
				xPos[i] = polygon.get(i).getX();
				zPos[i] = polygon.get(i).getZ();
			}
			
			Polygon poly = new Polygon(xPos, zPos, polygon.size());
			
			List<Block> contained = new ArrayList<Block>();
			
			int minX = MathUtil.getMin(xPos), maxX = MathUtil.getMax(xPos) + 1;
			int minZ = MathUtil.getMin(zPos), maxZ = MathUtil.getMax(zPos) + 1;
			
			for(int x = minX; x < maxX; x++)
				for(int z = minZ; z < maxZ; z++)
					if(poly.contains(x, z))
						contained.add(block.getWorld().getBlockAt(x, block.getY(), z));
			
			for(Block containedBlock : contained)
			{
				
			}
		}*/
		
	}

	public long getLastFarm()
	{
		return lastFarm;
	}

	public boolean isFirst()
	{
		return first;
	}

	@Override
	public void onJoin()
	{

	}

	@Override
	public void onLeave()
	{

	}
	
	public static void __DEBUG()
	{
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		System.out.println(stackTraceElements[2].toString());
	}
}
