package interview.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "pets")
public class Pet {

	@Id
	private int id;
	private Category category;
	private String name;
	private String[] photoUrls;
	private Tag[] tags;
	private String status;

	public Pet(int id, Category category, String name, String[] photoUrls, Tag[] tags, String status) {
		this.id = id;
		this.category = category;
		this.name = name;
		this.photoUrls = photoUrls;
		this.tags = tags;
		this.status = status;
	}

	public Pet() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Category getCategory() {
		return this.category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getPhotoUrls() {
		return this.photoUrls;
	}

	public void setPhotoUrls(String[] photoUrls) {
		this.photoUrls = photoUrls;
	}

	public Tag[] getTags() {
		return this.tags;
	}

	public void setTags(Tag[] tags) {
		this.tags = tags;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		if(!status.equals("available") && !status.equals("pending") && !status.equals("sold")){
			this.status = "available";
		}else{
			this.status = status;
		}
	}
}
