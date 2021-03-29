CREATE OR REPLACE FUNCTION public.magna_auditor_trigger()
	RETURNS trigger
	LANGUAGE plpgsql
AS $function$
	BEGIN

	if (TG_OP = 'UPDATE') then
		new.update_at = now();
		new.update_by = 'test';
		new.version = 1 + old.version;
	elsif  (TG_OP = 'INSERT') then
		new.create_at = now();
		new.update_at = now();
		new.version = 1;
		new.create_by = 'test';
		new.update_by = 'test';

	end if;

	return new;


	END;
$function$
